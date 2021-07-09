package com.epam.esm.web.controller;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.maintenance.UserService;
import com.epam.esm.web.pagination.PaginationManager;
import com.epam.esm.web.security.AccessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
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

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Class controller for interacting with {@link UserDto} objects.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final AccessValidator accessValidator;
    private final PaginationManager<UserDto> paginationManager;

    /**
     * Constructor with service and pagination manager
     *  @param service           user service
     * @param accessValidator access validator
     * @param paginationManager user pagination manager
     */
    @Autowired
    public UserController(UserService service, AccessValidator accessValidator,
                          PaginationManager<UserDto> paginationManager) {
        this.service = service;
        this.accessValidator = accessValidator;
        this.paginationManager = paginationManager;
    }

    /**
     * Gets all users
     *
     * @param page page number
     * @param size number of items per page
     * @return list of {@link UserDto}
     */
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

    /**
     * Gets user by id
     *
     * @param id user id
     * @return {@link UserDto} with given id
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UserDto receiveUser(@PathVariable int id) {
        accessValidator.validate(id);
        UserDto user = service.read(id);
        Link selfLink = createSelfLink(user);
        user.add(selfLink);
        return user;
    }

    /**
     * Gets all user orders
     *
     * @param id user id
     * @return list of {@link OrderDto}
     */
    @GetMapping(value = "/{id}/orders", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<OrderDto> receiveUserOrders(@PathVariable int id) {
        accessValidator.validate(id);
        return service.readUserOrders(id);
    }

    /**
     * Updates user
     * @param id user id
     * @param user user data
     * @return updated user
     */
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UserDto updateUser(@PathVariable int id, @RequestBody UserDto user) {
        accessValidator.validate(id);
        user.setId(id);
        UserDto updatedUser = service.update(user);
        Link selfLink = createSelfLink(updatedUser);
        updatedUser.add(selfLink);
        return updatedUser;
    }

    /**
     * creates user
     * @param user user data
     * @return created user
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public UserDto createUser(@RequestBody UserDto user) {
        UserDto createdUser = service.create(user);
        Link selfLink = createSelfLink(createdUser);
        createdUser.add(selfLink);
        return createdUser;
    }

    /**
     * Deletes user
     * @param id user id
     * @return server response
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
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
