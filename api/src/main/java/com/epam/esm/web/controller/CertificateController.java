package com.epam.esm.web.controller;

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
import java.util.Collections;
import java.util.List;

/**
 * Class controller for interacting with {@link GiftCertificateDto} objects.
 */
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private static final String DESCENDING_SORT_TYPE_PARAMETER = "desc";
    private static final String ASCENDING_SORT_TYPE_PARAMETER = "asc";
    private static final String NAME_SORT_VALUE_PARAMETER = "name";
    private static final String DATE_SORT_VALUE_PARAMETER = "date";
    private GiftCertificateService service;

    /**
     * Sets {@link GiftCertificateService}
     *
     * @param service - {@link GiftCertificateService} object
     */
    @Autowired
    public void setService(GiftCertificateService service) {
        this.service = service;
    }

    /**
     * Gets all {@link GiftCertificateDto} objects taking into account search parameters
     *
     * @param tag         tag name
     * @param sortObject  by which objects to sort
     * @param sortType    sorting type (asc/desc)
     * @param searchType  by which parameter to search
     * @param searchValue value to search
     * @return - list of {@link GiftCertificateDto}
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> receiveAllGiftCertificates(
            @RequestParam(required = false, name = "tag") String tag,
            @RequestParam(required = false, name = "sort_by") String sortObject,
            @RequestParam(required = false, name = "order_by") String sortType,
            @RequestParam(required = false, name = "search_by") String searchType,
            @RequestParam(required = false, name = "search_value") String searchValue) {
        List<GiftCertificateDto> certificates;
        if (searchType != null) {
            certificates = searchByParams(searchType, searchValue);
        } else {
            certificates = service.readAll();
        }
        if (tag != null) {
            List<GiftCertificateDto> certificatesByTag = service.searchByTagName(tag);
            certificates.retainAll(certificatesByTag);
        }
        processCertificatesSort(certificates, sortObject, sortType);
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
        service.delete(id);
        return ResponseEntity.noContent().build();
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

    private List<GiftCertificateDto> searchByParams(String searchType, String searchValue) {
        List<GiftCertificateDto> certificates;
        switch (searchType) {
            case "tag":
                certificates = service.searchByTagName(searchValue);
                break;
            case "name":
                certificates = service.searchByPartOfName(searchValue);
                break;
            case "description":
                certificates = service.searchByPartOfDescription(searchValue);
                break;
            default:
                certificates = Collections.emptyList();
        }
        return certificates;
    }

    private void processCertificatesSort(List<GiftCertificateDto> certificates, String sortObject, String sortType) {
        if (NAME_SORT_VALUE_PARAMETER.equals(sortObject)) {
            processSortByName(certificates, sortType);
        } else if (DATE_SORT_VALUE_PARAMETER.equals(sortObject)) {
            processSortByDate(certificates, sortType);
        }
    }

    private void processSortByName(List<GiftCertificateDto> certificates, String sortType) {
        if (ASCENDING_SORT_TYPE_PARAMETER.equals(sortType)) {
            service.sortByNameAscending(certificates);
        } else if (DESCENDING_SORT_TYPE_PARAMETER.equals(sortType)) {
            service.sortByNameDescending(certificates);
        }
    }

    private void processSortByDate(List<GiftCertificateDto> certificates, String sortType) {
        if (ASCENDING_SORT_TYPE_PARAMETER.equals(sortType)) {
            service.sortByDateAscending(certificates);
        } else if (DESCENDING_SORT_TYPE_PARAMETER.equals(sortType)) {
            service.sortByDateDescending(certificates);
        }
    }
}
