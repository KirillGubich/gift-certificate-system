package com.epam.esm.repository.dao;

import com.epam.esm.repository.criteria.GiftCertificateCriteria;
import com.epam.esm.repository.exception.GiftCertificateDuplicateException;
import com.epam.esm.repository.model.DatabaseInfo;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.SortType;
import com.epam.esm.repository.model.SortValue;
import com.epam.esm.repository.model.Tag;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<GiftCertificate> read(int id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        return giftCertificate == null ? Optional.empty() : Optional.of(giftCertificate);
    }

    private GiftCertificate readByName(String name) {
        TypedQuery<GiftCertificate> query = entityManager
                .createNamedQuery("GiftCertificate_findByName", GiftCertificate.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    public List<GiftCertificate> readAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    public List<GiftCertificate> readWithParameters(Integer page, Integer size, SortValue sortValue,
                                                    SortType sortType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        if (SortType.DESCENDING.equals(sortType)) {
            query.orderBy(criteriaBuilder.desc(root.get(sortValue.getFieldName())));
        } else {
            query.orderBy(criteriaBuilder.asc(root.get(sortValue.getFieldName())));
        }
        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);
        if (page != null && size != null) {
            typedQuery.setFirstResult((page - 1) * size);
            typedQuery.setMaxResults(size);
        }
        return typedQuery.getResultList();
    }

    @Override
    public GiftCertificate create(GiftCertificate entity) {
        try {
            entityManager.merge(entity);
            entityManager.flush();
        } catch (DuplicateKeyException e) {
            throw new GiftCertificateDuplicateException(entity.getName());
        }
        return readByName(entity.getName());
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

    public int fetchNumberOfPages(int size) {
        Query query = entityManager.createNamedQuery("GiftCertificate_getAmount");
        Long count = (Long) query.getSingleResult();
        int pages = count.intValue() / size;
        if (count % size > 0) {
            pages++;
        }
        return pages;
    }

    public List<GiftCertificate> searchByCriteria(GiftCertificateCriteria criteria, Integer page, Integer size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root).distinct(true);
        buildQuery(criteria, criteriaBuilder, query, root);
        SortValue sortValue = criteria.getSortValue();
        if (SortType.DESCENDING.equals(criteria.getSortType())) {
            query.orderBy(criteriaBuilder.desc(root.get(sortValue.getFieldName())));
        } else {
            query.orderBy(criteriaBuilder.asc(root.get(sortValue.getFieldName())));
        }
        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);
        if (page != null) {
            typedQuery.setFirstResult((page - 1) * size);
        }
        if (size != null) {
            typedQuery.setMaxResults(size);
        }
        return typedQuery.getResultList();
    }

    private void buildQuery(GiftCertificateCriteria criteria, CriteriaBuilder criteriaBuilder,
                            CriteriaQuery<GiftCertificate> query, Root<GiftCertificate> root) {
        List<Predicate> predicates = createPredicates(criteria, criteriaBuilder, query, root);
        Predicate[] predicatesArray = new Predicate[predicates.size()];
        predicates.toArray(predicatesArray);
        query.where(predicatesArray);
    }

    private List<Predicate> createPredicates(GiftCertificateCriteria criteria, CriteriaBuilder criteriaBuilder,
                                             CriteriaQuery<GiftCertificate> query, Root<GiftCertificate> root) {
        Join<GiftCertificate, Tag> tags = root.join(DatabaseInfo.TAG_TABLE);
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getName() != null) {
            Predicate predicateForName = criteriaBuilder.like(root.get(DatabaseInfo.CERTIFICATE_NAME_COLUMN),
                    "%" + criteria.getName() + "%");
            predicates.add(predicateForName);
        }
        if (criteria.getDescription() != null) {
            Predicate predicateForDescription =
                    criteriaBuilder.like(root.get(DatabaseInfo.CERTIFICATE_DESCRIPTION_COLUMN),
                            "%" + criteria.getDescription() + "%");
            predicates.add(predicateForDescription);
        }
        if (criteria.getTagNames() != null && !(criteria.getTagNames().isEmpty())) {
            CriteriaBuilder.In<String> inTags = criteriaBuilder.in(tags.get(DatabaseInfo.TAG_NAME_COLUMN));
            List<String> tagNames = criteria.getTagNames();
            for (String tagName : tagNames) {
                inTags.value(tagName);
            }
            predicates.add(inTags);
            query.groupBy(root.get("id"));
            query.having(criteriaBuilder.equal(criteriaBuilder.count(root.get("id")), tagNames.size()));
        }
        return predicates;
    }
}
