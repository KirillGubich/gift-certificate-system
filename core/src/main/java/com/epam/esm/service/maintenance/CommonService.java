package com.epam.esm.service.maintenance;

import java.util.List;
import java.util.Optional;

public interface CommonService<T> {

    boolean create(T entity);

    Optional<T> read(int id);

    List<T> readAll();

    Optional<T> update(T entity);

    boolean delete(int id);
}
