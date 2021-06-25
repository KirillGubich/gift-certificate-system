package com.epam.esm.web.controller;

import com.epam.esm.repository.criteria.GiftCertificateCriteria;
import com.epam.esm.repository.model.SortType;
import com.epam.esm.repository.model.SortValue;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.maintenance.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * Class controller for interacting with {@link GiftCertificateDto} objects.
 */
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final GiftCertificateService service;

    /**
     * Constructor with service
     * @param service {@link GiftCertificateService} object
     */
    @Autowired
    public CertificateController(GiftCertificateService service) {
        this.service = service;
    }

    /**
     * Gets all {@link GiftCertificateDto} objects taking into account search parameters
     *
     * @param tags        tag names
     * @param sortObject  by which objects to sort
     * @param sortOrder    sorting type (asc/desc)
     * @param name  certificate name or part of it
     * @param description certificate description or part of it
     * @param page page number
     * @param size amount of items on page
     * @return - list of {@link GiftCertificateDto}
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> receiveAllGiftCertificates(
            @RequestParam(required = false, name = "tags") List<String> tags,
            @RequestParam(required = false, name = "sort_by") String sortObject,
            @RequestParam(required = false, name = "order_by") String sortOrder,
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "description") String description,
            @RequestParam(required = false, name = "page") @Positive @Digits(integer = 4, fraction = 0) Integer page,
            @RequestParam(required = false, name = "size") @Positive @Digits(integer = 4, fraction = 0) Integer size) {
        List<GiftCertificateDto> certificates;
        SortType sortType = SortType.of(sortOrder);
        SortValue sortValue = SortValue.of(sortObject);
        boolean hasSearchParameters = tags != null || name != null || description != null;
        boolean needSort = sortObject != null || sortOrder != null;
        boolean needPagination = page != null && size != null;
        if (hasSearchParameters) {
            GiftCertificateCriteria criteria = buildCriteria(tags, name, description, sortType, sortValue);
            certificates = service.searchByCriteria(criteria, page, size);
        } else if (needSort || needPagination) {
            certificates = service.readWithParameters(page, size, sortValue, sortType);
        } else {
            certificates = service.readAll();
        }
        return certificates;
    }

    /**
     * Gets {@link GiftCertificateDto} by id
     *
     * @param id {@link GiftCertificateDto} id
     * @return {@link GiftCertificateDto} with given id
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto receiveGiftCertificate(@PathVariable int id) {
        return service.read(id);
    }

    /**
     * Creates new gift certificate
     *
     * @param giftCertificate {@link GiftCertificateDto} with create parameters
     * @return newly created {@link GiftCertificateDto}
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto createGiftCertificate(@Valid @RequestBody GiftCertificateDto giftCertificate) {
        return service.create(giftCertificate);
    }

    /**
     * Deletes gift certificate by given id
     *
     * @param id gift certificate id
     * @return server response
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteGiftCertificate(@PathVariable int id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

    /**
     * Updates gift certificate
     *
     * @param id              gift certificate id
     * @param giftCertificate {@link GiftCertificateDto} object with updated parameters
     * @return updated {@link GiftCertificateDto}
     */
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto updateGiftCertificate(@PathVariable int id,
                                                    @RequestBody GiftCertificateDto giftCertificate) {
        giftCertificate.setId(id);
        return service.update(giftCertificate);
    }

    private GiftCertificateCriteria buildCriteria(List<String> tags, String name, String description,
                                                  SortType sortType, SortValue sortValue) {
        return GiftCertificateCriteria.builder()
                .withName(name)
                .withDescription(description)
                .withTagNames(tags)
                .withSortType(sortType)
                .withSortValue(sortValue)
                .build();
    }
}
