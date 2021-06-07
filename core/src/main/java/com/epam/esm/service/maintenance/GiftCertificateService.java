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
    public boolean create(GiftCertificateDto entity) {
        final Set<Tag> tags = entity.getTags();
        processForNewTags(tags);
        for (Tag tag : tags) {
            certificateDao.addTagToCertificate(entity.getId(), tag.getId());
        }
        return certificateDao.create(mapToModel(entity));
    }

    @Override
    public Optional<GiftCertificateDto> read(int id) {
        final Optional<GiftCertificate> giftCertificate = certificateDao.read(id);
        return giftCertificate.map(this::mapToDto);
    }

    @Override
    public List<GiftCertificateDto> readAll() {
        List<GiftCertificate> certificates = certificateDao.readAll();
        return certificates.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDto update(GiftCertificateDto entity) {
        final int id = entity.getId();
        final Optional<GiftCertificate> giftCertificateOptional = certificateDao.read(id);
        if (!giftCertificateOptional.isPresent()) {
            throw new RuntimeException(); //todo custom exception
        }
        final GiftCertificate oldCertificate = giftCertificateOptional.get();
        GiftCertificate giftCertificate = createUpdatedEntity(entity, oldCertificate);
        processUpdatedTags(id, oldCertificate.getTags(), entity.getTags());
        return mapToDto(certificateDao.update(giftCertificate));
    }

    @Override
    public boolean delete(int id) {
        return certificateDao.delete(id);
    }

    public List<GiftCertificateDto> searchByTagName(String tagName) {
        final Optional<Tag> tagOptional = tagDao.readByName(tagName);
        if (!tagOptional.isPresent()) {
            //todo handle
            throw new RuntimeException();
        }
        int tagId = tagOptional.get().getId();
        List<GiftCertificate> certificates = certificateDao.fetchCertificatesByTagId(tagId);
        return certificates.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> searchByPartOfName(String partOfName) {
        List<GiftCertificate> certificates = certificateDao.fetchCertificatesByPartOfName(partOfName);
        return certificates.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> searchByPartOfDescription(String partOfDescription) {
        List<GiftCertificate> certificates = certificateDao.fetchCertificatesByPartOfDescription(partOfDescription);
        return certificates.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> sortByNameAscending(List<GiftCertificateDto> certificates) {
        certificates.sort(Comparator.comparing(GiftCertificateDto::getName));
        return certificates;
    }

    public List<GiftCertificateDto> sortByNameDescending(List<GiftCertificateDto> certificates) {
        certificates.sort(Comparator.comparing(GiftCertificateDto::getName).reversed());
        return certificates;
    }

    public List<GiftCertificateDto> sortByDateAscending(List<GiftCertificateDto> certificates) {
        certificates.sort(Comparator.comparing(GiftCertificateDto::getCreateDate));
        return certificates;
    }

    public List<GiftCertificateDto> sortByDateDescending(List<GiftCertificateDto> certificates) {
        certificates.sort(Comparator.comparing(GiftCertificateDto::getCreateDate).reversed());
        return certificates;
    }

    private void processForNewTags(Set<Tag> tags) {
        for (Tag tag : tags) {
            final Optional<Tag> optionalTag = tagDao.readByName(tag.getName());
            if (!optionalTag.isPresent()) {
                tagDao.create(tag);
            }
        }
    }

    private void processUpdatedTags(int certificateId, Set<Tag> oldTags, Set<Tag> newTags) {
        Set<Tag> tagsToRemove = new HashSet<>(oldTags);
        tagsToRemove.removeAll(newTags);
        Set<Tag> tagsToAdd =new HashSet<>(newTags);
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
        String description = entity.getDescription().isEmpty() ?
                oldEntity.getDescription() : entity.getDescription();
        BigDecimal price = entity.getPrice() == null ? oldEntity.getPrice() : entity.getPrice();
        Period duration = entity.getDuration() == null ?
                oldEntity.getDuration() : entity.getDuration();
        LocalDateTime createDate = entity.getCreateDate() == null ?
                oldEntity.getCreateDate() : entity.getCreateDate();
        LocalDateTime lastUpdateDate = entity.getLastUpdateDate() == null ?
                oldEntity.getLastUpdateDate() : entity.getLastUpdateDate();
        return GiftCertificate.builder()
                .withId(entity.getId())
                .withName(name)
                .withDescription(description)
                .withPrice(price)
                .withDuration(duration)
                .withCreateDate(createDate)
                .withLastUpdateDate(lastUpdateDate)
                .build();
    }

    private GiftCertificateDto mapToDto(GiftCertificate certificate) {
        return GiftCertificateDto.builder()
                .withId(certificate.getId())
                .withName(certificate.getName())
                .withDescription(certificate.getDescription())
                .withPrice(certificate.getPrice())
                .withDuration(certificate.getDuration())
                .withCreateDate(certificate.getCreateDate())
                .withLastUpdateDate(certificate.getLastUpdateDate())
                .withTags(certificate.getTags())
                .build();
    }

    private GiftCertificate mapToModel(GiftCertificateDto certificate) {
        return GiftCertificate.builder()
                .withId(certificate.getId())
                .withName(certificate.getName())
                .withDescription(certificate.getDescription())
                .withPrice(certificate.getPrice())
                .withDuration(certificate.getDuration())
                .withCreateDate(certificate.getCreateDate())
                .withLastUpdateDate(certificate.getLastUpdateDate())
                .withTags(certificate.getTags())
                .build();
    }
}
