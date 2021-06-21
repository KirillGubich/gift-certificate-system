package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao implements CommonDao<User> {

    private static final String FIND_ALL_QUERY = "SELECT u FROM User as u";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> read(int id) {
        User user = entityManager.find(User.class, id);
        return user == null ? Optional.empty() : Optional.of(user);
    }

    @Override
    public List<User> readAll() {
        TypedQuery<User> query = entityManager.createQuery(FIND_ALL_QUERY, User.class);
        return query.getResultList();
    }

    @Override
    public User create(User entity) {
        throw new UnsupportedOperationException("User: create");
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("User: delete");
    }
}
