package com.epam.esm.web.pagination;

import org.springframework.hateoas.CollectionModel;

public interface PaginationManager<T> {
    String NEXT_PAGE_PARAMETER = "next";
    String PREVIOUS_PAGE_PARAMETER = "prev";
    String LAST_PAGE_PARAMETER = "last";

    void fillParameters(CollectionModel<T> collection, int page, int size, int lastPage);

    default boolean hasNextPage(int page, int lastPage) {
        return page < lastPage;
    }

    default boolean hasPreviousPage(int page) {
        return page > 1;
    }
}
