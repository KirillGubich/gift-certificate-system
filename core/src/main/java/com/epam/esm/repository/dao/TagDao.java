package com.epam.esm.repository.dao;

import com.epam.esm.repository.exception.AbsenceOfNewlyCreatedException;
import com.epam.esm.repository.exception.TagDuplicateException;
import com.epam.esm.repository.model.Tag;
import org.hibernate.Hibernate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDao implements CommonDao<Tag> {

    private static final String FIND_BY_NAME_QUERY = "SELECT t FROM Tag as t WHERE t.name=:name";
    private static final String FIND_ALL_QUERY = "SELECT t FROM Tag as t";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tag> read(int id) {
        Tag tag = entityManager.find(Tag.class, id);
        return tag == null ? Optional.empty() : Optional.of(tag);
    }

    @Transactional
    public Optional<Tag> readByName(String name, boolean needCertificates) {
        try {
            TypedQuery<Tag> query = entityManager
                    .createQuery(FIND_BY_NAME_QUERY, Tag.class);
            query.setParameter("name", name);
            Tag tag = query.getSingleResult();
            if (needCertificates) {
                Hibernate.initialize(tag.getCertificates());
            }
            return Optional.of(tag);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Tag> readByName(String name) {
        return readByName(name, false);
    }

    @Override
    public List<Tag> readAll() {
        TypedQuery<Tag> query = entityManager.createQuery(FIND_ALL_QUERY, Tag.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Tag create(Tag entity) {
        try {
            entityManager.persist(entity);
            entityManager.flush();
        } catch (DuplicateKeyException e) {
            throw new TagDuplicateException(entity.getName());
        }
        return readByName(entity.getName()).orElseThrow(AbsenceOfNewlyCreatedException::new);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        Optional<Tag> tag = read(id);
        tag.ifPresent(value -> entityManager.remove(value));
        return tag.isPresent();
    }
}
