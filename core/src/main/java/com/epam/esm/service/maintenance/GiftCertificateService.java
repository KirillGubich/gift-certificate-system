package com.epam.esm.service.maintenance;

import com.epam.esm.repository.criteria.GiftCertificateCriteria;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.SortType;
import com.epam.esm.repository.model.SortValue;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchCertificateException;
import com.epam.esm.service.exception.NoSuchPageException;
import com.epam.esm.service.exception.NotExistentUpdateException;
import com.epam.esm.service.validation.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ConversionService conversionService;

    @Autowired
    public GiftCertificateService(GiftCertificateValidator validator, GiftCertificateDao certificateDao,
                                  TagDao tagDao, ConversionService conversionService) {
        this.validator = validator;
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.conversionService = conversionService;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        if (dto.getTags() == null) {
            dto.setTags(new HashSet<>());
        }
        GiftCertificate entity = conversionService.convert(dto, GiftCertificate.class);
        if (entity == null) {
            throw new IllegalArgumentException("null");
        }
        Set<Tag> tags = processTags(dto.getTags());
        entity.setTags(tags);
        GiftCertificate giftCertificate = certificateDao.create(entity);
        return conversionService.convert(giftCertificate, GiftCertificateDto.class);
    }

    @Override
    public GiftCertificateDto read(int id) {
        final Optional<GiftCertificate> giftCertificate = certificateDao.read(id);
        GiftCertificate certificate = giftCertificate
                .orElseThrow(() -> new NoSuchCertificateException(id));
        return conversionService.convert(certificate, GiftCertificateDto.class);
    }

    @Override
    public List<GiftCertificateDto> readAll() {
        List<GiftCertificate> certificates = certificateDao.readAll();
        return certificates.stream()
                .map(certificate -> conversionService.convert(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> readWithParameters(Integer page, Integer size, SortValue sortValue,
                                                       SortType sortType) {
        if (size != null && page != null) {
            int numberOfPages = certificateDao.fetchNumberOfPages(size);
            if (page > numberOfPages) {
                throw new NoSuchPageException(page);
            }
        }
        List<GiftCertificate> certificates = certificateDao.readWithParameters(page, size, sortValue, sortType);
        return certificates.stream()
                .map(certificate -> conversionService.convert(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        if (dto.getTags() == null) {
            dto.setTags(new HashSet<>());
        }
        Optional<GiftCertificate> giftCertificateOptional = certificateDao.read(dto.getId());
        GiftCertificate entity = giftCertificateOptional
                .orElseThrow(() -> new NotExistentUpdateException(dto.getId()));
        updateEntity(entity, dto);
        Set<Tag> tags = processTags(dto.getTags());
        entity.setTags(tags);
        return conversionService.convert(certificateDao.update(entity), GiftCertificateDto.class);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return certificateDao.delete(id);
    }

    public List<GiftCertificateDto> searchByCriteria(GiftCertificateCriteria criteria, Integer page, Integer size) {
        List<GiftCertificate> certificates = certificateDao.searchByCriteria(criteria, page, size);
        return certificates.stream()
                .map(certificate -> conversionService.convert(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    public int getLastPage(int size) {
        return certificateDao.fetchNumberOfPages(size);
    }

    private void updateEntity(GiftCertificate entity, GiftCertificateDto dto) {
        if (dto.getName() != null) {
            entity.setName(validator.validateName(dto.getName()));
        }
        if (dto.getDescription() != null) {
            entity.setDescription(validator.validateDescription(dto.getDescription()));
        }
        if (dto.getDuration() != null) {
            entity.setDuration(validator.validateDuration(dto.getDuration()));
        }
        if (dto.getPrice() != null) {
            entity.setPrice(validator.validatePrice(dto.getPrice()));
        }
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
