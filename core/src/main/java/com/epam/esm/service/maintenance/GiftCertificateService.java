package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.converter.GiftCertificateConverter;
import com.epam.esm.service.converter.GiftCertificateDtoConverter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchCertificateException;
import com.epam.esm.service.exception.NotExistentUpdateException;
import com.epam.esm.service.validation.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateService implements CommonService<GiftCertificateDto> {

    private final GiftCertificateValidator validator;
    private final GiftCertificateDao certificateDao;
    private final TagDao tagDao;
    private final GiftCertificateConverter certificateConverter;
    private final GiftCertificateDtoConverter certificateDtoConverter;

    @Autowired
    public GiftCertificateService(GiftCertificateValidator validator, GiftCertificateDao certificateDao, TagDao tagDao,
                                  GiftCertificateConverter certificateConverter,
                                  GiftCertificateDtoConverter certificateDtoConverter) {
        this.validator = validator;
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.certificateConverter = certificateConverter;
        this.certificateDtoConverter = certificateDtoConverter;
    }

    @Override
    public GiftCertificateDto create(GiftCertificateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        if (dto.getTags() == null) {
            dto.setTags(new HashSet<>());
        }
        GiftCertificate entity = certificateDtoConverter.convert(dto);
        if (entity == null) {
            throw new IllegalArgumentException("null");
        }
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateDate(now);
        entity.setLastUpdateDate(now);
        Set<Tag> tags = processTags(dto.getTags());
        entity.setTags(tags);
        GiftCertificate giftCertificate = certificateDao.create(entity);
        return certificateConverter.convert(giftCertificate);
    }

    @Override
    public GiftCertificateDto read(int id) {
        final Optional<GiftCertificate> giftCertificate = certificateDao.read(id);
        if (!giftCertificate.isPresent()) {
            throw new NoSuchCertificateException(id);
        }
        return certificateConverter.convert(giftCertificate.get());
    }

    @Override
    public List<GiftCertificateDto> readAll() {
        Iterable<GiftCertificate> all = certificateDao.readAll();
        List<GiftCertificate> certificateList = new ArrayList<>();
        for (GiftCertificate certificate : all) {
            certificateList.add(certificate);
        }
        return certificateList.stream().map(certificateConverter::convert).collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDto update(GiftCertificateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        if (dto.getTags() == null) {
            dto.setTags(new HashSet<>());
        }
        Optional<GiftCertificate> giftCertificateOptional = certificateDao.read(dto.getId());
        if (!giftCertificateOptional.isPresent()) {
            throw new NotExistentUpdateException(dto.getId());
        }
        GiftCertificate entity = giftCertificateOptional.get();
        updateEntity(entity, dto);
        Set<Tag> tags = processTags(dto.getTags());
        entity.setTags(tags);
        entity.setLastUpdateDate(LocalDateTime.now());
        return certificateConverter.convert(certificateDao.update(entity));
    }

    @Override
    public boolean delete(int id) {
        return certificateDao.delete(id);
    }

    public List<GiftCertificateDto> searchByTagName(String tagName) {
        if (tagName == null) {
            return Collections.emptyList();
        }
        Optional<Tag> tagOptional = tagDao.readByName(tagName, true);
        if (!tagOptional.isPresent()) {
            return Collections.emptyList();
        }
        Tag tag = tagOptional.get();
        List<GiftCertificate> certificates = tag.getCertificates();
        return certificates.stream()
                .map(certificateConverter::convert)
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> searchByPartOfName(String partOfName) {
        if (partOfName == null) {
            return Collections.emptyList();
        }
        List<GiftCertificate> certificates = certificateDao.fetchCertificatesByPartOfName(partOfName);
        return certificates.stream()
                .map(certificateConverter::convert)
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> searchByPartOfDescription(String partOfDescription) {
        if (partOfDescription == null) {
            return Collections.emptyList();
        }
        List<GiftCertificate> certificates = certificateDao.fetchCertificatesByPartOfDescription(partOfDescription);
        return certificates.stream()
                .map(certificateConverter::convert)
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

    private void updateEntity(GiftCertificate entity, GiftCertificateDto dto) {
        String name = dto.getName() == null ? entity.getName() : dto.getName();
        String description = dto.getDescription() == null ? entity.getDescription() : dto.getDescription();
        BigDecimal price = dto.getPrice() == null ? entity.getPrice() : dto.getPrice();
        int durationInDays = dto.getDuration() == null ? entity.getDuration() : dto.getDuration();
        entity.setName(validator.validateName(name));
        entity.setDescription(validator.validateDescription(description));
        entity.setPrice(validator.validatePrice(price));
        entity.setDuration(validator.validateDuration(durationInDays));
    }

    private Set<Tag> processTags(Set<TagDto> tags) {
        Set<Tag> updatedTags = new HashSet<>();
        for (TagDto tagDto : tags) {
            Optional<Tag> tagOptional = tagDao.readByName(tagDto.getName());
            if (!tagOptional.isPresent()) {
                Tag tag = new Tag();
                tag.setName(tagDto.getName());
                updatedTags.add(tagDao.create(tag));
            } else {
                updatedTags.add(tagOptional.get());
            }
        }
        return updatedTags;
    }
}
