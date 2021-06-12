package com.epam.esm.repository.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface with common DAO operations
 * @param <T> type parameter
 */
public interface CommonDao<T> {

    /**
     * Reads entity by id
     * @param id entity id
     * @return optional of entity or empty optional if nothing was found
     */
    Optional<T> read(int id);

    /**
     * Reads all entities
     * @return list with all entities
     */
    List<T> readAll();

    /**
     * Creates new entity
     * @param entity entity with parameters
     * @return newly created entity
     */
    T create(T entity);

    /**
     * Deletes entity by id
     * @param id entity id
     * @return true if deleted successfully, else - false
     */
    boolean delete(int id);
}
