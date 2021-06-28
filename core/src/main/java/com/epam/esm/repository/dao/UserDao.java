package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao implements CommonDao<User> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> read(int id) {
        User user = entityManager.find(User.class, id);
        return user == null ? Optional.empty() : Optional.of(user);
    }

    @Override
    public List<User> readAll() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User as u", User.class);
        return query.getResultList();
    }

    public List<User> readPaginated(int page, int size) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User as u", User.class);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
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

    public int fetchNumberOfPages(int size) {
        Query query = entityManager.createQuery("SELECT count(u.id) FROM User as u");
        Long count = (Long) query.getSingleResult();
        int pages = count.intValue() / size;
        if (count % size > 0) {
            pages++;
        }
        return pages;
    }
}
