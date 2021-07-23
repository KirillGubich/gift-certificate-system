package com.epam.esm.web.pagination;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.controller.TagController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagPaginationManager implements PaginationManager<TagDto> {

    @Override
    public void fillParameters(CollectionModel<TagDto> collection, int page, int size, int lastPage) {
        if (hasPreviousPage(page)) {
            collection.add(linkTo(methodOn(TagController.class)
                    .receiveAllTags(page - 1, size))
                    .withRel(PREVIOUS_PAGE_PARAMETER));
        }
        if (hasNextPage(page, lastPage)) {
            collection.add(linkTo(methodOn(TagController.class)
                    .receiveAllTags(page + 1, size))
                    .withRel(NEXT_PAGE_PARAMETER));
        }
        collection.add(linkTo(methodOn(TagController.class)
                .receiveAllTags(lastPage, size))
                .withRel(LAST_PAGE_PARAMETER));
    }
}
