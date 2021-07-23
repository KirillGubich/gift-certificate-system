package com.epam.esm.web.pagination;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.web.controller.OrderController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderPaginationManager implements PaginationManager<OrderDto> {

    @Override
    public void fillParameters(CollectionModel<OrderDto> collection, int page, int size, int lastPage) {
        if (hasPreviousPage(page)) {
            collection.add(linkTo(methodOn(OrderController.class)
                    .receiveAllOrders(page - 1, size))
                    .withRel(PREVIOUS_PAGE_PARAMETER));
        }
        if (hasNextPage(page, lastPage)) {
            collection.add(linkTo(methodOn(OrderController.class)
                    .receiveAllOrders(page + 1, size))
                    .withRel(NEXT_PAGE_PARAMETER));
        }
        collection.add(linkTo(methodOn(OrderController.class)
                .receiveAllOrders(lastPage, size))
                .withRel(LAST_PAGE_PARAMETER));
    }
}
