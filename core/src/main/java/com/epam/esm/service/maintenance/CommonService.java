package com.epam.esm.service.maintenance;

import java.util.List;
import java.util.Optional;

public interface CommonService<T> {

    boolean create(T dto);

    Optional<T> read(int id);

    List<T> readAll();

    T update(T dto);

    boolean delete(int id);
}
