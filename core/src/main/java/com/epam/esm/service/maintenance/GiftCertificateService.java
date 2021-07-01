package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchCertificateException;
import com.epam.esm.service.exception.NotExistentUpdateException;
import com.epam.esm.service.validation.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Period;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateService implements CommonService<GiftCertificateDto> {

    private GiftCertificateDao certificateDao;
    private TagDao tagDao;
    private GiftCertificateValidator validator;

    @Autowired
    public void setCertificateDao(GiftCertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Autowired
    public void setValidator(GiftCertificateValidator validator) {
        this.validator = validator;
    }

    @Override
    public GiftCertificateDto create(GiftCertificateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        Set<TagDto> tagsDto = dto.getTags();
        Set<Tag> tags = processForNewTags(tagsDto);
        GiftCertificate giftCertificate = certificateDao.create(mapToModel(dto));
        int certificateId = giftCertificate.getId();
        for (Tag tag : tags) {
            certificateDao.addTagToCertificate(certificateId, tag.getId());
        }
        GiftCertificateDto certificateDto = mapToDto(giftCertificate);
        certificateDto.setTags(tags.stream()
                .map(this::mapTagToDto).collect(Collectors.toSet()));
        return certificateDto;
    }

    @Override
    public GiftCertificateDto read(int id) {
        final Optional<GiftCertificate> giftCertificate = certificateDao.read(id);
        if (!giftCertificate.isPresent()) {
            throw new NoSuchCertificateException(id);
        }
        return mapToDto(giftCertificate.get());
    }

    @Override
    public List<GiftCertificateDto> readAll() {
        List<GiftCertificate> certificates = certificateDao.readAll();
        return certificates.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDto update(GiftCertificateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        final int id = dto.getId();
        final Optional<GiftCertificate> giftCertificateOptional = certificateDao.read(id);
        if (!giftCertificateOptional.isPresent()) {
            throw new NotExistentUpdateException(id);
        }
        final GiftCertificate oldCertificate = giftCertificateOptional.get();
        GiftCertificate giftCertificate = createUpdatedEntity(dto, oldCertificate);
        Set<TagDto> oldTags = oldCertificate.getTags().stream()
                .map(this::mapTagToDto)
                .collect(Collectors.toSet());
        processUpdatedTags(id, oldTags, dto.getTags());
        return mapToDto(certificateDao.update(giftCertificate));
    }

    @Override
    public boolean delete(int id) {
        Optional<GiftCertificate> certificate = certificateDao.read(id);
        if (!certificate.isPresent()) {
            throw new NoSuchCertificateException(id);
        }
        deleteCertificateConnectionWithTags(id, certificate.get());
        return certificateDao.delete(id);
    }

    public List<GiftCertificateDto> searchByTagName(String tagName) {
        if (tagName == null) {
            return Collections.emptyList();
        }
        Optional<Tag> tagOptional = tagDao.readByName(tagName);
        if (!tagOptional.isPresent()) {
            return Collections.emptyList();
        }
        int tagId = tagOptional.get().getId();
        List<GiftCertificate> certificates = certificateDao.fetchCertificatesByTagId(tagId);
        return certificates.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> searchByPartOfName(String partOfName) {
        if (partOfName == null) {
            return Collections.emptyList();
        }
        List<GiftCertificate> certificates = certificateDao.fetchCertificatesByPartOfName(partOfName);
        return certificates.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> searchByPartOfDescription(String partOfDescription) {
        if (partOfDescription == null) {
            return Collections.emptyList();
        }
        List<GiftCertificate> certificates = certificateDao.fetchCertificatesByPartOfDescription(partOfDescription);
        return certificates.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void sortByNameAscending(List<GiftCertificateDto> certificates) {
        certificates.sort(Comparator.comparing(GiftCertificateDto::getName));
    }

    public void sortByNameDescending(List<GiftCertificateDto> certificates) {
        certificates.sort(Comparator.comparing(GiftCertificateDto::getName).reversed());
    }

    public void sortByDateAscending(List<GiftCertificateDto> certificates) {
        certificates.sort(Comparator.comparing(GiftCertificateDto::getCreateDate));
    }

    public void sortByDateDescending(List<GiftCertificateDto> certificates) {
        certificates.sort(Comparator.comparing(GiftCertificateDto::getCreateDate).reversed());
    }

    private Set<Tag> processForNewTags(Set<TagDto> tags) {
        Set<Tag> updatedTags = new HashSet<>();
        for (TagDto tag : tags) {
            Optional<Tag> optionalTag = tagDao.readByName(tag.getName());
            if (optionalTag.isPresent()) {
                updatedTags.add(optionalTag.get());
            } else {
                updatedTags.add(tagDao.create(mapTagDtoToEntity(tag)));
            }
        }
        return updatedTags;
    }

    private void processUpdatedTags(int certificateId, Set<TagDto> oldTags, Set<TagDto> newTags) {
        if (newTags == null) {
            newTags = new HashSet<>();
        }
        Set<TagDto> tagsToRemove = new HashSet<>(oldTags);
        tagsToRemove.removeAll(newTags);
        Set<TagDto> tagsToAdd = new HashSet<>(newTags);
        tagsToAdd.removeAll(oldTags);
        for (TagDto tag : tagsToRemove) {
            certificateDao.removeTagFromCertificate(certificateId, tag.getId());
        }
        Set<Tag> tags = processForNewTags(tagsToAdd);
        for (Tag tag : tags) {
            certificateDao.addTagToCertificate(certificateId, tag.getId());
        }
    }

    private GiftCertificate createUpdatedEntity(GiftCertificateDto entity, GiftCertificate oldEntity) {
        String name = entity.getName() == null ? oldEntity.getName() : entity.getName();
        String description = entity.getDescription() == null ?
                oldEntity.getDescription() : entity.getDescription();
        BigDecimal price = entity.getPrice() == null ? oldEntity.getPrice() : entity.getPrice();
        int durationInDays = entity.getDuration() == null ?
                oldEntity.getDuration().getDays() : entity.getDuration();
        name = validator.validateName(name);
        description = validator.validateDescription(description);
        price = validator.validatePrice(price);
        durationInDays = validator.validateDuration(durationInDays);
        Period duration = Period.ofDays(durationInDays);
        return GiftCertificate.builder()
                .withId(entity.getId())
                .withName(name)
                .withDescription(description)
                .withPrice(price)
                .withDuration(duration)
                .withCreateDate(oldEntity.getCreateDate())
                .withLastUpdateDate(oldEntity.getLastUpdateDate())
                .build();
    }

    private void deleteCertificateConnectionWithTags(int id, GiftCertificate certificate) {
        Set<Tag> tags = certificate.getTags();
        for (Tag tag : tags) {
            certificateDao.removeTagFromCertificate(id, tag.getId());
        }
    }

    private GiftCertificateDto mapToDto(GiftCertificate certificate) {
        return GiftCertificateDto.builder()
                .withId(certificate.getId())
                .withName(certificate.getName())
                .withDescription(certificate.getDescription())
                .withPrice(certificate.getPrice())
                .withDuration(certificate.getDuration().getDays())
                .withCreateDate(certificate.getCreateDate().toString())
                .withLastUpdateDate(certificate.getLastUpdateDate().toString())
                .withTags(certificate.getTags().stream()
                        .map(this::mapTagToDto).collect(Collectors.toSet()))
                .build();
    }

    private GiftCertificate mapToModel(GiftCertificateDto certificate) {
        return GiftCertificate.builder()
                .withId(certificate.getId())
                .withName(certificate.getName())
                .withDescription(certificate.getDescription())
                .withPrice(certificate.getPrice())
                .withDuration(Period.ofDays(certificate.getDuration()))
                .withTags(certificate.getTags().stream()
                        .map(this::mapTagDtoToEntity).collect(Collectors.toSet()))
                .build();
    }

    private TagDto mapTagToDto(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }

    private Tag mapTagDtoToEntity(TagDto tagDto) {
        return new Tag(tagDto.getId(), tagDto.getName());
    }
}
