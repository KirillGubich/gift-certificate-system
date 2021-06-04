package com.epam.esm.repository.dao;

import java.util.List;
import java.util.Optional;

public interface CommonDao<T> {

    Optional<T> read(int id);

    List<T> readAll();

    boolean create(T entity);

    T update(T entity);

    boolean delete(int id);
}
