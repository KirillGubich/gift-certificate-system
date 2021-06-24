package com.epam.esm.repository.dao;

import com.epam.esm.repository.criteria.GiftCertificateCriteria;
import com.epam.esm.repository.exception.GiftCertificateDuplicateException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDao implements CommonDao<GiftCertificate> {

    private static final String FIND_ALL_QUERY = "SELECT c FROM GiftCertificate as c";

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

    public List<GiftCertificate> searchByCriteria(GiftCertificateCriteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root).distinct(true);
        buildQuery(criteria, criteriaBuilder, query, root);
        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    private void buildQuery(GiftCertificateCriteria criteria, CriteriaBuilder criteriaBuilder,
                            CriteriaQuery<GiftCertificate> query, Root<GiftCertificate> root) {
        Join<GiftCertificate, Tag> tags = root.join("tags");
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getName() != null) {
            Predicate predicateForName = criteriaBuilder.like(root.get("name"), "%" + criteria.getName() + "%");
            predicates.add(predicateForName);
        }
        if (criteria.getDescription() != null) {
            Predicate predicateForDescription =
                    criteriaBuilder.like(root.get("description"), "%" + criteria.getDescription() + "%");
            predicates.add(predicateForDescription);
        }
        if (criteria.getTagNames() != null && !(criteria.getTagNames().isEmpty())) {
            CriteriaBuilder.In<String> inTags = criteriaBuilder.in(tags.get("name"));
            List<String> tagNames = criteria.getTagNames();
            for (String tagName : tagNames) {
                inTags.value(tagName);
            }
            predicates.add(inTags);
            query.groupBy(root.get("id"));
            query.having(criteriaBuilder.equal(criteriaBuilder.count(root.get("id")), tagNames.size()));
        }
        Predicate[] predicatesArray = new Predicate[predicates.size()];
        predicates.toArray(predicatesArray);
        query.where(predicatesArray);
    }
}
