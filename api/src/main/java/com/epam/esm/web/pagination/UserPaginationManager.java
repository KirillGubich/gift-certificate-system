package com.epam.esm.web.pagination;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.controller.UserController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserPaginationManager implements PaginationManager<UserDto> {

    @Override
    public void fillParameters(CollectionModel<UserDto> collection, int page, int size, int lastPage) {
        if (hasPreviousPage(page)) {
            collection.add(linkTo(methodOn(UserController.class)
                    .receiveAllUsers(page - 1, size))
                    .withRel(PREVIOUS_PAGE_PARAMETER));
        }
        if (hasNextPage(page, lastPage)) {
            collection.add(linkTo(methodOn(UserController.class)
                    .receiveAllUsers(page + 1, size))
                    .withRel(NEXT_PAGE_PARAMETER));
        }
        collection.add(linkTo(methodOn(UserController.class)
                .receiveAllUsers(lastPage, size))
                .withRel(LAST_PAGE_PARAMETER));
    }
}
