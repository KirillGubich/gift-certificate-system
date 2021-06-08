package com.epam.esm.service.maintenance;

import java.util.List;

public interface CommonService<T> {

    T create(T dto);

    T read(int id);

    List<T> readAll();

    T update(T dto);

    boolean delete(int id);
}
