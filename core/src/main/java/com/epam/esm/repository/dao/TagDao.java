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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tag> read(int id) {
        Tag tag = entityManager.find(Tag.class, id);
        return tag == null ? Optional.empty() : Optional.of(tag);
    }

    public Optional<Tag> readByName(String name) {
        try {
            TypedQuery<Tag> query = entityManager.createNamedQuery("Tag_findByName", Tag.class);
            query.setParameter("name", name);
            Tag tag = query.getSingleResult();
            return Optional.of(tag);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> readAll() {
        TypedQuery<Tag> query = entityManager.createNamedQuery("Tag_findAll", Tag.class);
        return query.getResultList();
    }

    public List<Tag> readPaginated(int page, int size) {
        TypedQuery<Tag> query = entityManager.createNamedQuery("Tag_findAll", Tag.class);
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
        Query query = entityManager.createNamedQuery("Tag_getAmount");
        Long count = (Long) query.getSingleResult();
        int pages = count.intValue() / size;
        if (count % size > 0) {
            pages++;
        }
        return pages;
    }

    public Optional<Tag> readMostWidelyUsedTag() {
        Query query = entityManager.createNamedQuery("Tag_getMostUsedTag", Tag.class);
        Tag tag = (Tag) query.getSingleResult();
        return tag == null ? Optional.empty() : Optional.of(tag);
    }
}
