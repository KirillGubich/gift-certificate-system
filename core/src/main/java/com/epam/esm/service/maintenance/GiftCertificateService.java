package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Autowired
    public void setCertificateDao(GiftCertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public GiftCertificateDto create(GiftCertificateDto dto) {
        Set<Tag> tags = dto.getTags();
        tags = processForNewTags(tags);
        GiftCertificate giftCertificate = certificateDao.create(mapToModel(dto));
        int certificateId = giftCertificate.getId();
        for (Tag tag : tags) {
            certificateDao.addTagToCertificate(certificateId, tag.getId());
        }
        GiftCertificateDto certificateDto = mapToDto(giftCertificate);
        certificateDto.setTags(tags);
        return certificateDto;
    }

    @Override
    public GiftCertificateDto read(int id) {
        final Optional<GiftCertificate> giftCertificate = certificateDao.read(id);
        if (!giftCertificate.isPresent()) {
            //todo throw custom Exception
            throw new RuntimeException();
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
        final int id = dto.getId();
        final Optional<GiftCertificate> giftCertificateOptional = certificateDao.read(id);
        if (!giftCertificateOptional.isPresent()) {
            throw new RuntimeException(); //todo custom exception
        }
        final GiftCertificate oldCertificate = giftCertificateOptional.get();
        GiftCertificate giftCertificate = createUpdatedEntity(dto, oldCertificate);
        processUpdatedTags(id, oldCertificate.getTags(), dto.getTags());
        return mapToDto(certificateDao.update(giftCertificate));
    }

    @Override
    public boolean delete(int id) {
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

    private Set<Tag> processForNewTags(Set<Tag> tags) {
        Set<Tag> updatedTags = new HashSet<>();
        for (Tag tag : tags) {
            Optional<Tag> optionalTag = tagDao.readByName(tag.getName());
            if (!optionalTag.isPresent()) {
                updatedTags.add(tagDao.create(tag));
            } else {
                updatedTags.add(optionalTag.get());
            }
        }
        return updatedTags;
    }

    private void processUpdatedTags(int certificateId, Set<Tag> oldTags, Set<Tag> newTags) {
        Set<Tag> tagsToRemove = new HashSet<>(oldTags);
        tagsToRemove.removeAll(newTags);
        Set<Tag> tagsToAdd = new HashSet<>(newTags);
        tagsToAdd.removeAll(oldTags);
        for (Tag tag : tagsToRemove) {
            certificateDao.removeTagFromCertificate(certificateId, tag.getId());
        }
        processForNewTags(tagsToAdd);
        for (Tag tag : tagsToAdd) {
            certificateDao.addTagToCertificate(certificateId, tag.getId());
        }
    }

    private GiftCertificate createUpdatedEntity(GiftCertificateDto entity, GiftCertificate oldEntity) {
        String name = entity.getName().isEmpty() ? oldEntity.getName() : entity.getName();
        String description = entity.getDescription() == null ?
                oldEntity.getDescription() : entity.getDescription();
        BigDecimal price = entity.getPrice() == null ? oldEntity.getPrice() : entity.getPrice();
        Period duration = entity.getDuration() == null ?
                oldEntity.getDuration() : Period.ofDays(entity.getDuration());
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

    private GiftCertificateDto mapToDto(GiftCertificate certificate) {
        return GiftCertificateDto.builder()
                .withId(certificate.getId())
                .withName(certificate.getName())
                .withDescription(certificate.getDescription())
                .withPrice(certificate.getPrice())
                .withDuration(certificate.getDuration().getDays())
                .withCreateDate(certificate.getCreateDate().toString())
                .withLastUpdateDate(certificate.getLastUpdateDate().toString())
                .withTags(certificate.getTags())
                .build();
    }

    private GiftCertificate mapToModel(GiftCertificateDto certificate) {
        return GiftCertificate.builder()
                .withId(certificate.getId())
                .withName(certificate.getName())
                .withDescription(certificate.getDescription())
                .withPrice(certificate.getPrice())
                .withDuration(Period.ofDays(certificate.getDuration()))
                .withTags(certificate.getTags())
                .build();
    }
}
