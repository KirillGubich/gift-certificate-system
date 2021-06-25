package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.maintenance.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

/**
 * Class controller for interacting with {@link TagDto} objects.
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService service;

    /**
     * Constructor with service
     *
     * @param service {@link TagService} object
     */
    @Autowired
    public TagController(TagService service) {
        this.service = service;
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
    public List<TagDto> receiveAllTags(
            @RequestParam(required = false, name = "page") @Positive @Digits(integer = 4, fraction = 0) Integer page,
            @RequestParam(required = false, name = "size") @Positive @Digits(integer = 4, fraction = 0) Integer size) {
        if (page == null || size == null) {
            return service.readAll();
        }
        return service.readPaginated(page, size);
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
        return service.read(id);
    }

    /**
     * Creates tag
     *
     * @param tagDto {@link TagDto} object with parameters
     * @return newly created {@link TagDto}
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@Valid @RequestBody TagDto tagDto) {
        return service.create(tagDto);
    }

    /**
     * Deletes tag by id
     *
     * @param id tag id
     * @return server response
     */
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteTag(@PathVariable int id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
