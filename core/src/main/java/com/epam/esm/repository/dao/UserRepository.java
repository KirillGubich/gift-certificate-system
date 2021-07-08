package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByLogin(String name);
}
