package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.maintenance.TagService;
import com.epam.esm.web.pagination.PaginationManager;
import com.epam.esm.web.pagination.TagPaginationManager;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Class controller for interacting with {@link TagDto} objects.
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService service;
    private final PaginationManager<TagDto> paginationManager;

    /**
     * Constructor with service
     *
     * @param service           {@link TagService} object
     * @param paginationManager {@link TagPaginationManager} object
     */
    @Autowired
    public TagController(TagService service, PaginationManager<TagDto> paginationManager) {
        this.service = service;
        this.paginationManager = paginationManager;
    }

    /**
     * Receive tags
     *
     * @param page page number
     * @param size number of items per page
     * @return list of {@link TagDto}
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<TagDto> receiveAllTags(
            @RequestParam(required = false, name = "page") @Positive @Digits(integer = 4, fraction = 0) Integer page,
            @RequestParam(required = false, name = "size") @Positive @Digits(integer = 4, fraction = 0) Integer size) {
        List<TagDto> tags;
        boolean needPagination = page != null && size != null;
        tags = needPagination ? service.readPaginated(page, size) : service.readAll();
        addSelfLinks(tags);
        Link link = linkTo(TagController.class).withSelfRel();
        CollectionModel<TagDto> collectionModel = CollectionModel.of(tags, link);
        if (needPagination) {
            int lastPage = service.getLastPage(size);
            paginationManager.fillParameters(collectionModel, page, size, lastPage);
        }
        return collectionModel;
    }

    /**
     * Gets tag by id
     *
     * @param id tag id
     * @return {@link TagDto} with given id
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public TagDto receiveTag(@PathVariable int id) {
        TagDto tag = service.read(id);
        Link selfLink = createSelfLink(tag);
        tag.add(selfLink);
        return tag;
    }

    /**
     * Gets the most widely used tag of a user with the highest cost of all orders
     *
     * @return most widely used tag
     */
    @GetMapping(value = "/popular", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public TagDto receiveMostWidelyUsedTag() {
        TagDto tag = service.receiveMostUsedTag();
        Link selfLink = createSelfLink(tag);
        tag.add(selfLink);
        return tag;
    }

    /**
     * Creates tag
     *
     * @param tagDto {@link TagDto} object with parameters
     * @return newly created {@link TagDto}
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody TagDto tagDto) {
        TagDto tag = service.create(tagDto);
        Link selfLink = createSelfLink(tag);
        tag.add(selfLink);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(selfLink.toUri());
        return new ResponseEntity<>(tag, headers, HttpStatus.CREATED);
    }

    /**
     * Deletes tag by id
     *
     * @param id tag id
     * @return server response
     */
    @DeleteMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTag(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void addSelfLinks(List<TagDto> tags) {
        for (TagDto tag : tags) {
            Link selfLink = createSelfLink(tag);
            tag.add(selfLink);
        }
    }

    private Link createSelfLink(TagDto tag) {
        return linkTo(TagController.class)
                .slash(tag.getId())
                .withSelfRel();
    }
}
