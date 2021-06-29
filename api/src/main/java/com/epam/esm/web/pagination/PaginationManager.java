package com.epam.esm.web.pagination;

import org.springframework.hateoas.CollectionModel;

/**
 * Interface for pagination managing.
 * @param <T> entity to interact with
 */
public interface PaginationManager<T> {
    String NEXT_PAGE_PARAMETER = "next";
    String PREVIOUS_PAGE_PARAMETER = "prev";
    String LAST_PAGE_PARAMETER = "last";

    /**
     * Fills pagination parameters, such as next page, previous page and last page
     * @param collection list of entities
     * @param page page number
     * @param size amount of items per page
     * @param lastPage last page number
     */
    void fillParameters(CollectionModel<T> collection, int page, int size, int lastPage);

    /**
     * Checks for the existence of the next page
     * @param page page number
     * @param lastPage last page number
     * @return true if next page exist, else - false
     */
    default boolean hasNextPage(int page, int lastPage) {
        return page < lastPage;
    }

    /**
     * Checks for the existence of the previous page
     * @param page page number
     * @return true if previous page exist, else - false
     */
    default boolean hasPreviousPage(int page) {
        return page > 1;
    }
}
