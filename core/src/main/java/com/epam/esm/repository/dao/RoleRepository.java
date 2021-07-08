package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByName(String name);
}
