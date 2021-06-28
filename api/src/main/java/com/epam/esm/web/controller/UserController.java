package com.epam.esm.web.controller;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.maintenance.UserService;
import com.epam.esm.web.pagination.PaginationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final PaginationManager<UserDto> paginationManager;

    @Autowired
    public UserController(UserService service, PaginationManager<UserDto> paginationManager) {
        this.service = service;
        this.paginationManager = paginationManager;
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<UserDto> receiveAllUsers(
            @RequestParam(required = false, name = "page") @Positive @Digits(integer = 4, fraction = 0) Integer page,
            @RequestParam(required = false, name = "size") @Positive @Digits(integer = 4, fraction = 0) Integer size) {
        List<UserDto> users;
        boolean needPagination = page != null && size != null;
        users = needPagination ? service.readPaginated(page, size) : service.readAll();
        addSelfLinks(users);
        Link link = linkTo(UserController.class).withSelfRel();
        CollectionModel<UserDto> collectionModel = CollectionModel.of(users, link);
        if (needPagination) {
            int lastPage = service.getLastPage(size);
            paginationManager.fillParameters(collectionModel, page, size, lastPage);
        }
        return collectionModel;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UserDto receiveUser(@PathVariable int id) {
        UserDto user = service.read(id);
        Link selfLink = createSelfLink(user);
        user.add(selfLink);
        return user;
    }

    @GetMapping(value = "/{id}/orders", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> receiveUserOrders(@PathVariable int id) {
        return service.readUserOrders(id);
    }

    private void addSelfLinks(List<UserDto> users) {
        for (UserDto user : users) {
            Link selfLink = createSelfLink(user);
            user.add(selfLink);
        }
    }

    private Link createSelfLink(UserDto user) {
        return linkTo(UserController.class)
                .slash(user.getId())
                .withSelfRel();
    }
}
