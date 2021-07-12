package com.epam.esm.web.controller;

import com.epam.esm.repository.criteria.GiftCertificateCriteria;
import com.epam.esm.repository.model.SortType;
import com.epam.esm.repository.model.SortValue;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.maintenance.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.util.List;

import static com.epam.esm.web.pagination.PaginationManager.LAST_PAGE_PARAMETER;
import static com.epam.esm.web.pagination.PaginationManager.NEXT_PAGE_PARAMETER;
import static com.epam.esm.web.pagination.PaginationManager.PREVIOUS_PAGE_PARAMETER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Class controller for interacting with {@link GiftCertificateDto} objects.
 */
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final GiftCertificateService service;

    /**
     * Constructor with service
     *
     * @param service {@link GiftCertificateService} object
     */
    @Autowired
    public CertificateController(GiftCertificateService service) {
        this.service = service;
    }

    /**
     * Gets all {@link GiftCertificateDto} objects taking into account search parameters
     *
     * @param request     http request
     * @param tags        tag names
     * @param sortObject  by which objects to sort
     * @param sortOrder   sorting type (asc/desc)
     * @param name        certificate name or part of it
     * @param description certificate description or part of it
     * @param page        page number
     * @param size        amount of items on page
     * @return - list of {@link GiftCertificateDto}
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<GiftCertificateDto> receiveAllGiftCertificates(
            HttpServletRequest request,
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
        addSelfLinks(certificates);
        Link link = linkTo(CertificateController.class).withSelfRel();
        CollectionModel<GiftCertificateDto> collection = CollectionModel.of(certificates, link);
        if (needPagination && !hasSearchParameters) {
            String url = request.getRequestURL().toString() + "?" + request.getQueryString();
            createPageLinks(url, page, size, collection);
        }
        return collection;
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
        GiftCertificateDto certificate = service.read(id);
        Link selfLink = createSelfLink(certificate);
        certificate.add(selfLink);
        return certificate;
    }

    /**
     * Creates new gift certificate
     *
     * @param giftCertificate {@link GiftCertificateDto} with create parameters
     * @return newly created {@link GiftCertificateDto}
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GiftCertificateDto> createGiftCertificate(
            @Valid @RequestBody GiftCertificateDto giftCertificate) {
        GiftCertificateDto certificate = service.create(giftCertificate);
        Link selfLink = createSelfLink(certificate);
        certificate.add(selfLink);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(selfLink.toUri());
        return new ResponseEntity<>(certificate, headers, HttpStatus.CREATED);
    }

    /**
     * Deletes gift certificate by given id
     *
     * @param id gift certificate id
     * @return server response
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public GiftCertificateDto updateGiftCertificate(@PathVariable int id,
                                                    @RequestBody GiftCertificateDto giftCertificate) {
        giftCertificate.setId(id);
        GiftCertificateDto certificate = service.update(giftCertificate);
        Link selfLink = createSelfLink(certificate);
        certificate.add(selfLink);
        return certificate;
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

    private void createPageLinks(String url, int page, int size, CollectionModel<GiftCertificateDto> collection) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        int lastPage = service.getLastPage(size);
        if (hasPreviousPage(page)) {
            String prevPageUri = uriBuilder
                    .replaceQueryParam("page", page - 1)
                    .toUriString();
            Link prevLink = Link.of(prevPageUri, PREVIOUS_PAGE_PARAMETER);
            collection.add(prevLink);
        }
        if (hasNextPage(page, lastPage)) {
            String nextPageUri = uriBuilder
                    .replaceQueryParam("page", page + 1)
                    .toUriString();
            Link nextLink = Link.of(nextPageUri, NEXT_PAGE_PARAMETER);
            collection.add(nextLink);
        }
        String lastPageUri = uriBuilder
                .replaceQueryParam("page", lastPage)
                .toUriString();
        Link lastLink = Link.of(lastPageUri, LAST_PAGE_PARAMETER);
        collection.add(lastLink);
    }

    private void addSelfLinks(List<GiftCertificateDto> certificates) {
        for (GiftCertificateDto certificate : certificates) {
            Link selfLink = createSelfLink(certificate);
            certificate.add(selfLink);
        }
    }

    private Link createSelfLink(GiftCertificateDto certificate) {
        return linkTo(CertificateController.class)
                .slash(certificate.getId())
                .withSelfRel();
    }

    private boolean hasNextPage(int page, int lastPage) {
        return page < lastPage;
    }

    private boolean hasPreviousPage(int page) {
        return page > 1;
    }
}
