package com.epam.esm.repository.dao;

import com.epam.esm.repository.exception.TagDuplicateException;
import com.epam.esm.repository.model.Tag;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDao implements CommonDao<Tag> {

    private static final String MOST_USED_TAG_QUERY = "SELECT id, name FROM tags LEFT JOIN certificate_tag ct " +
            "on tags.id = ct.tag_id WHERE certificate_id IN (SELECT co.certificate_id FROM gift_certificates JOIN " +
            "certificate_order co on gift_certificates.id = co.certificate_id WHERE order_id IN " +
            "(SELECT orders.order_id FROM orders WHERE user_id = (SELECT users.user_id FROM users " +
            "RIGHT JOIN orders o ON users.user_id = o.user_id GROUP BY users.user_id " +
            "ORDER BY SUM(cost) desc LIMIT 1))) GROUP BY id ORDER BY COUNT(id) desc LIMIT 1";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tag> read(int id) {
        Tag tag = entityManager.find(Tag.class, id);
        return tag == null ? Optional.empty() : Optional.of(tag);
    }

    public Optional<Tag> readByName(String name) {
        try {
            TypedQuery<Tag> query = entityManager
                    .createQuery("SELECT t FROM Tag as t WHERE t.name=:name", Tag.class);
            query.setParameter("name", name);
            Tag tag = query.getSingleResult();
            return Optional.of(tag);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> readAll() {
        TypedQuery<Tag> query = entityManager.createQuery("SELECT t FROM Tag as t", Tag.class);
        return query.getResultList();
    }

    public List<Tag> readPaginated(int page, int size) {
        TypedQuery<Tag> query = entityManager.createQuery("SELECT t FROM Tag as t", Tag.class);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public Tag create(Tag entity) {
        try {
            entityManager.persist(entity);
            entityManager.flush();
        } catch (DuplicateKeyException e) {
            throw new TagDuplicateException(entity.getName());
        }
        return entity;
    }

    @Override
    public boolean delete(int id) {
        Optional<Tag> tag = read(id);
        tag.ifPresent(entityManager::remove);
        return tag.isPresent();
    }

    public int fetchNumberOfPages(int size) {
        Query query = entityManager.createQuery("SELECT count(t.id) FROM Tag as t");
        Long count = (Long) query.getSingleResult();
        int pages = count.intValue() / size;
        if (count % size > 0) {
            pages++;
        }
        return pages;
    }

    public Optional<Tag> readMostWidelyUsedTag() {
        Query query = entityManager.createNativeQuery(MOST_USED_TAG_QUERY, Tag.class);
        Tag tag = (Tag) query.getSingleResult();
        return tag == null ? Optional.empty() : Optional.of(tag);
    }
}
