package com.epam.esm.repository.dao;

import com.epam.esm.repository.exception.GiftCertificateDuplicateException;
import com.epam.esm.repository.model.GiftCertificate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDao implements CommonDao<GiftCertificate> {

    private static final String FIND_ALL_QUERY = "SELECT c FROM GiftCertificate as c";
    private static final String FIND_BY_PART_OF_NAME_QUERY =
            "SELECT c FROM GiftCertificate as c WHERE c.name LIKE :pattern";
    private static final String FIND_BY_PART_OF_DESCRIPTION_QUERY =
            "SELECT c FROM GiftCertificate as c WHERE c.description LIKE :pattern";
    private static final String FIND_BY_NAME_QUERY = "SELECT c FROM GiftCertificate as c WHERE c.name=:name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<GiftCertificate> read(int id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        return giftCertificate == null ? Optional.empty() : Optional.of(giftCertificate);
    }

    @Override
    public List<GiftCertificate> readAll() {
        TypedQuery<GiftCertificate> query = entityManager.createQuery(FIND_ALL_QUERY,
                GiftCertificate.class);
        return query.getResultList();
    }

    @Override
    public GiftCertificate create(GiftCertificate entity) {
        try {
            entityManager.merge(entity);
            entityManager.flush();
        } catch (DuplicateKeyException e) {
            throw new GiftCertificateDuplicateException(entity.getName());
        }
        return entity;
    }

    @Override
    public boolean delete(int id) {
        Optional<GiftCertificate> certificate = read(id);
        certificate.ifPresent(entityManager::remove);
        return certificate.isPresent();
    }

    public GiftCertificate update(GiftCertificate entity) {
        entityManager.merge(entity);
        entityManager.flush();
        return entity;
    }

    public List<GiftCertificate> fetchCertificatesByPartOfName(String partOfName) {
        String pattern = "%" + partOfName + "%";
        TypedQuery<GiftCertificate> query = entityManager
                .createQuery(FIND_BY_PART_OF_NAME_QUERY,
                        GiftCertificate.class);
        query.setParameter("pattern", pattern);
        return query.getResultList();
    }

    public List<GiftCertificate> fetchCertificatesByPartOfDescription(String partOfDescription) {
        String pattern = "%" + partOfDescription + "%";
        TypedQuery<GiftCertificate> query = entityManager
                .createQuery(FIND_BY_PART_OF_DESCRIPTION_QUERY,
                        GiftCertificate.class);
        query.setParameter("pattern", pattern);
        return query.getResultList();
    }

    private Optional<GiftCertificate> readByName(String name) { //todo remove
        try {
            TypedQuery<GiftCertificate> query = entityManager
                    .createQuery(FIND_BY_NAME_QUERY, GiftCertificate.class);
            query.setParameter("name", name);
            GiftCertificate certificate = query.getSingleResult();
            return Optional.of(certificate);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
