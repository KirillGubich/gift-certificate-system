package com.epam.esm.service.maintenance;

import java.util.List;

/**
 * Interface with common CRUD service operations
 * @param <T> type parameter
 */
public interface CommonService<T> {

    /**
     * Creates entity
     * @param dto entity with parameters
     * @return newly created entity
     */
    T create(T dto);

    /**
     * Reads entity by given id
     * @param id entity id
     * @return entity with given id
     */
    T read(int id);

    /**
     * Reads all entities
     * @return list of entities
     */
    List<T> readAll();

    /**
     * Updates entity
     * @param dto entity with parameters
     * @return updated entity
     */
    T update(T dto);

    /**
     * Deletes entity by id
     * @param id entity id
     * @return true if deleted successfully, else - false
     */
    boolean delete(int id);
}
