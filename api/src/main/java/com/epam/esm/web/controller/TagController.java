package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.maintenance.CommonService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Class controller for interacting with {@link TagDto} objects.
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    private CommonService<TagDto> service;

    /**
     * Sets {@link TagService}
     *
     * @param service {@link TagService} object
     */
    @Autowired
    public void setService(CommonService<TagDto> service) {
        this.service = service;
    }

    /**
     * Gets all tags
     *
     * @return list of {@link TagDto}
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> receiveAllTags() {
        return service.readAll();
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
     * Deletes tag by given id
     *
     * @param id tag id
     * @return server response
     */
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteTag(@PathVariable int id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
