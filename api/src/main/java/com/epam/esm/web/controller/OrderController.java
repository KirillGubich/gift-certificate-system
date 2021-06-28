package com.epam.esm.web.controller;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.maintenance.OrderService;
import com.epam.esm.web.pagination.PaginationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;
    private final PaginationManager<OrderDto> paginationManager;

    @Autowired
    public OrderController(OrderService service, PaginationManager<OrderDto> paginationManager) {
        this.service = service;
        this.paginationManager = paginationManager;
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<OrderDto> receiveAllOrders(
            @RequestParam(required = false, name = "page") @Positive @Digits(integer = 4, fraction = 0) Integer page,
            @RequestParam(required = false, name = "size") @Positive @Digits(integer = 4, fraction = 0) Integer size) {
        List<OrderDto> orders;
        boolean needPagination = page != null && size != null;
        orders = needPagination ? service.readPaginated(page, size) : service.readAll();
        addSelfLinks(orders);
        Link link = linkTo(UserController.class).withSelfRel();
        CollectionModel<OrderDto> collectionModel = CollectionModel.of(orders, link);
        if (needPagination) {
            int lastPage = service.getLastPage(size);
            paginationManager.fillParameters(collectionModel, page, size, lastPage);
        }
        return collectionModel;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto receiveOrder(@PathVariable int id) {
        OrderDto order = service.read(id);
        Link selfLink = createSelfLink(order);
        order.add(selfLink);
        return order;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        OrderDto order = service.create(orderDto);
        Link selfLink = createSelfLink(order);
        order.add(selfLink);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(selfLink.toUri());
        return new ResponseEntity<>(order, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto updateOrder(@PathVariable int id, @RequestBody OrderDto orderDto) {
        orderDto.setId(id);
        OrderDto order = service.update(orderDto);
        Link selfLink = createSelfLink(order);
        order.add(selfLink);
        return order;
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteOrder(@PathVariable int id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

    private void addSelfLinks(List<OrderDto> orders) {
        for (OrderDto order : orders) {
            Link selfLink = createSelfLink(order);
            order.add(selfLink);
        }
    }

    private Link createSelfLink(OrderDto order) {
        return linkTo(OrderController.class)
                .slash(order.getId())
                .withSelfRel();
    }
}
