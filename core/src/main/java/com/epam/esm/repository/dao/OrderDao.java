package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao implements CommonDao<Order> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Order> read(int id) {
        Order order = entityManager.find(Order.class, id);
        return order == null ? Optional.empty() : Optional.of(order);
    }

    @Override
    public List<Order> readAll() {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order as o", Order.class);
        return query.getResultList();
    }

    public List<Order> readPaginated(int page, int size) {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order as o", Order.class);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public Order create(Order entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    public boolean delete(int id) {
        Optional<Order> order = read(id);
        order.ifPresent(entityManager::remove);
        return order.isPresent();
    }

    public Order update(Order entity) {
        entityManager.merge(entity);
        entityManager.flush();
        return entity;
    }

    public int fetchNumberOfPages(int size) {
        Query query = entityManager.createQuery("SELECT count(o.id) FROM Order as o");
        Long count = (Long) query.getSingleResult();
        int pages = count.intValue() / size;
        if (count % size > 0) {
            pages++;
        }
        return pages;
    }
}
