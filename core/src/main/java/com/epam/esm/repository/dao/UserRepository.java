package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByLogin(String name);
}
