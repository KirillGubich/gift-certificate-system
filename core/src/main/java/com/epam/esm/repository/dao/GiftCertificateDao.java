package com.epam.esm.repository.dao;

import com.epam.esm.repository.exception.AbsenceOfNewlyCreatedException;
import com.epam.esm.repository.exception.GiftCertificateDuplicateException;
import com.epam.esm.repository.mapper.GiftCertificateMapper;
import com.epam.esm.repository.mapper.TagMapper;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GiftCertificateDao implements CommonDao<GiftCertificate> {

    private static final String GET_CERTIFICATE_BY_ID_SQL = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date FROM gift_certificates " +
            "WHERE id = ?";
    private static final String GET_CERTIFICATE_BY_NAME_SQL = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date FROM gift_certificates " +
            "WHERE name = ?";
    private static final String GET_ALL_CERTIFICATES_SQL = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date FROM gift_certificates";
    private static final String UPDATE_CERTIFICATE_BY_ID_SQL = "UPDATE gift_certificates " +
            "SET name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ? " +
            "WHERE id = ?";
    private static final String DELETE_CERTIFICATE_BY_ID_SQL = "DELETE FROM gift_certificates WHERE id = ?";
    private static final String CREATE_CERTIFICATE_SQL = "INSERT INTO gift_certificates " +
            "(name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_CERTIFICATE_TAGS_SQL = "SELECT tag_id, name FROM certificate_tag " +
            "LEFT JOIN tags ON certificate_tag.tag_id=tags.id WHERE certificate_id=?";
    private static final String GET_CERTIFICATES_BY_TAG_ID_SQL = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date FROM certificate_tag " +
            "LEFT JOIN gift_certificates ON certificate_tag.certificate_id=gift_certificates.id WHERE tag_id = ?";

    private static final String GET_CERTIFICATES_BY_NAME_PART_SQL = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date FROM gift_certificates " +
            "WHERE name LIKE ?";
    private static final String GET_CERTIFICATES_BY_DESCRIPTION_PART_SQL = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date FROM gift_certificates " +
            "WHERE description LIKE ?";
    private static final String ADD_TAG_TO_CERTIFICATE_SQL = "INSERT INTO certificate_tag (certificate_id, tag_id) " +
            "VALUES (?, ?)";
    private static final String DELETE_CERTIFICATE_TAG_SQL = "DELETE FROM certificate_tag WHERE certificate_id = ? " +
            "and tag_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<GiftCertificate> read(int id) {
        List<GiftCertificate> queryResult =
                jdbcTemplate.query(GET_CERTIFICATE_BY_ID_SQL, new GiftCertificateMapper(), id);
        Optional<GiftCertificate> giftCertificate = queryResult.stream().findFirst();
        if (giftCertificate.isPresent()) {
            Set<Tag> tags = fetchCertificateTags(id);
            giftCertificate.get().setTags(tags);
        }
        return giftCertificate;
    }

    @Override
    public List<GiftCertificate> readAll() {
        List<GiftCertificate> certificates = jdbcTemplate.query(GET_ALL_CERTIFICATES_SQL, new GiftCertificateMapper());
        fillCertificateTags(certificates);
        return certificates;
    }

    @Override
    public GiftCertificate create(GiftCertificate entity) {
        LocalDateTime now = LocalDateTime.now();
        try {
            jdbcTemplate.update(CREATE_CERTIFICATE_SQL,
                    entity.getName(),
                    entity.getDescription(),
                    entity.getPrice(),
                    entity.getDuration().getDays(),
                    now,
                    now);
        } catch (DuplicateKeyException e) {
            throw new GiftCertificateDuplicateException(entity.getName());
        }
        return readByName(entity.getName()).orElseThrow(AbsenceOfNewlyCreatedException::new);
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_CERTIFICATE_BY_ID_SQL, id) > 0;
    }

    public GiftCertificate update(GiftCertificate entity) {
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(UPDATE_CERTIFICATE_BY_ID_SQL, entity.getName(), entity.getDescription(), entity.getPrice(),
                entity.getDuration().getDays(), entity.getCreateDate(), now, entity.getId());
        return read(entity.getId()).orElseThrow(AbsenceOfNewlyCreatedException::new);
    }

    public List<GiftCertificate> fetchCertificatesByTagId(int tagId) {
        final List<GiftCertificate> certificates = jdbcTemplate
                .query(GET_CERTIFICATES_BY_TAG_ID_SQL, new GiftCertificateMapper(), tagId);
        fillCertificateTags(certificates);
        return certificates;
    }

    public List<GiftCertificate> fetchCertificatesByPartOfName(String partOfName) {
        String pattern = "%" + partOfName + "%";
        final List<GiftCertificate> certificates = jdbcTemplate
                .query(GET_CERTIFICATES_BY_NAME_PART_SQL, new GiftCertificateMapper(), pattern);
        fillCertificateTags(certificates);
        return certificates;
    }

    public List<GiftCertificate> fetchCertificatesByPartOfDescription(String partOfDescription) {
        String pattern = "%" + partOfDescription + "%";
        final List<GiftCertificate> certificates = jdbcTemplate
                .query(GET_CERTIFICATES_BY_DESCRIPTION_PART_SQL, new GiftCertificateMapper(), pattern);
        fillCertificateTags(certificates);
        return certificates;
    }

    public void addTagToCertificate(int certificateId, int tagId) {
        jdbcTemplate.update(ADD_TAG_TO_CERTIFICATE_SQL, certificateId, tagId);
    }

    public void removeTagFromCertificate(int certificateId, int tagId) {
        jdbcTemplate.update(DELETE_CERTIFICATE_TAG_SQL, certificateId, tagId);
    }

    private Optional<GiftCertificate> readByName(String name) {
        List<GiftCertificate> queryResult =
                jdbcTemplate.query(GET_CERTIFICATE_BY_NAME_SQL, new GiftCertificateMapper(), name);
        Optional<GiftCertificate> giftCertificate = queryResult.stream().findFirst();
        if (giftCertificate.isPresent()) {
            Set<Tag> tags = fetchCertificateTags(giftCertificate.get().getId());
            giftCertificate.get().setTags(tags);
        }
        return giftCertificate;
    }

    private void fillCertificateTags(List<GiftCertificate> certificates) {
        for (GiftCertificate certificate : certificates) {
            final Set<Tag> tags = fetchCertificateTags(certificate.getId());
            certificate.setTags(tags);
        }
    }

    private Set<Tag> fetchCertificateTags(int id) {
        final List<Tag> tagsList =
                jdbcTemplate.query(GET_CERTIFICATE_TAGS_SQL, new TagMapper(), id);
        return new HashSet<>(tagsList);
    }
}
