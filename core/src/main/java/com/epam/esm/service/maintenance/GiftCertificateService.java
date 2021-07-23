package com.epam.esm.service.maintenance;

import com.epam.esm.repository.criteria.GiftCertificateCriteria;
import com.epam.esm.repository.dao.GiftCertificateRepository;
import com.epam.esm.repository.dao.TagRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateService implements CommonService<GiftCertificateDto> {
    private final GiftCertificateValidator validator;
    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final ConversionService conversionService;

    @Autowired
    public GiftCertificateService(GiftCertificateValidator validator,
                                  GiftCertificateRepository certificateRepository, TagRepository tagRepository,
                                  ConversionService conversionService) {
        this.validator = validator;
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
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
        GiftCertificate giftCertificate = certificateRepository.saveAndFlush(entity);
        return conversionService.convert(giftCertificate, GiftCertificateDto.class);
    }

    @Override
    public GiftCertificateDto read(int id) {
        final Optional<GiftCertificate> giftCertificate = certificateRepository.findById(id);
        GiftCertificate certificate = giftCertificate
                .orElseThrow(() -> new NoSuchCertificateException(id));
        return conversionService.convert(certificate, GiftCertificateDto.class);
    }

    @Override
    public List<GiftCertificateDto> readAll() {
        List<GiftCertificate> certificates = new ArrayList<>();
        final Iterable<GiftCertificate> allCertificates = certificateRepository.findAll();
        allCertificates.forEach(certificates::add);
        return certificates.stream()
                .map(certificate -> conversionService.convert(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> readWithParameters(Integer page, Integer size, SortValue sortValue,
                                                       SortType sortType) {
        Sort sort = extractSort(sortValue, sortType);
        List<GiftCertificate> certificates = new ArrayList<>();
        if (size != null && page != null) {
            checkPageOutOfRange(page, size);
            PageRequest pageRequest = PageRequest.of(page, size, sort);
            Page<GiftCertificate> certificatesPage = certificateRepository.findAll(pageRequest);
            certificates = certificatesPage.get().collect(Collectors.toList());
        } else {
            Iterable<GiftCertificate> certificatesIterable = certificateRepository.findAll(sort);
            certificatesIterable.forEach(certificates::add);
        }
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
        Optional<GiftCertificate> giftCertificateOptional = certificateRepository.findById(dto.getId());
        GiftCertificate entity = giftCertificateOptional
                .orElseThrow(() -> new NotExistentUpdateException(dto.getId()));
        updateEntity(entity, dto);
        Set<Tag> tags = processTags(dto.getTags());
        entity.setTags(tags);
        return conversionService.convert(certificateRepository.saveAndFlush(entity), GiftCertificateDto.class);
    }

    @Override
    @Transactional
    public void delete(int id) {
        certificateRepository.deleteById(id);
    }

    public List<GiftCertificateDto> searchByCriteria(GiftCertificateCriteria criteria, Integer page, Integer size) {
        Sort sort = extractSort(criteria.getSortValue(), criteria.getSortType());
        String name = criteria.getName() != null ? criteria.getName() : "";
        String description = criteria.getDescription() != null ? criteria.getDescription() : "";
        List<String> tagNames = criteria.getTagNames();
        boolean needPagination = page != null && size != null;
        boolean needSearchByTags = tagNames != null && !tagNames.isEmpty();
        List<GiftCertificate> certificates;
        if (needSearchByTags) {
            certificates = findCertificatesWithTags(page, size, sort, name, description, tagNames, needPagination);
        } else {
            certificates = findCertificatesWithoutTags(page, size, sort, name, description, needPagination);
        }
        return certificates.stream()
                .map(certificate -> conversionService.convert(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    public int getLastPage(int size) {
        int count = (int) certificateRepository.count();
        int pages = count / size;
        if (count % size > 0) {
            pages++;
        }
        return pages;
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
            Optional<Tag> tagOptional = tagRepository.findByName(tagDto.getName());
            if (!tagOptional.isPresent()) {
                Tag tag = new Tag();
                tag.setName(tagDto.getName());
                updatedTags.add(tagRepository.save(tag));
            } else {
                updatedTags.add(tagOptional.get());
            }
        }
        return updatedTags;
    }

    private Sort extractSort(SortValue sortValue, SortType sortType) {
        Sort sort;
        if (SortType.DESCENDING.equals(sortType)) {
            sort = Sort.by(sortValue.getFieldName()).descending();
        } else {
            sort = Sort.by(sortValue.getFieldName()).ascending();
        }
        return sort;
    }

    private void checkPageOutOfRange(Integer page, Integer size) {
        int numberOfPages = getLastPage(size);
        if (page > numberOfPages) {
            throw new NoSuchPageException(page);
        }
    }

    private List<GiftCertificate> findCertificatesWithoutTags(Integer page, Integer size, Sort sort, String name,
                                                              String description, boolean needPagination) {
        List<GiftCertificate> certificates;
        if (needPagination) {
            PageRequest pageRequest = PageRequest.of(page, size, sort);
            Page<GiftCertificate> pageCertificates = certificateRepository
                    .findAllByNameContainsAndDescriptionContains(name, description, pageRequest);
            certificates = pageCertificates.get().collect(Collectors.toList());
        } else {
            certificates = certificateRepository.findAllByNameContainsAndDescriptionContains(
                    name, description, sort);
        }
        return certificates;
    }

    private List<GiftCertificate> findCertificatesWithTags(Integer page, Integer size, Sort sort, String name,
                                                           String description, List<String> tagNames,
                                                           boolean needPagination) {
        List<GiftCertificate> certificates;
        if (needPagination) {
            PageRequest pageRequest = PageRequest.of(page, size, sort);
            Page<GiftCertificate> pageTags = certificateRepository
                    .findAllByNameContainsAndDescriptionContainsAndTagsNameIn(
                            name, description, tagNames, pageRequest);
            certificates = pageTags.get().collect(Collectors.toList());
        } else {
            certificates = certificateRepository
                    .findAllByNameContainsAndDescriptionContainsAndTagsNameIn(name, description, tagNames, sort);
        }
        return certificates;
    }
}
