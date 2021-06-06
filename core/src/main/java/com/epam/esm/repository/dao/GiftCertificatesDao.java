package com.epam.esm.repository.dao;

import com.epam.esm.repository.mapper.CertificateIdMapper;
import com.epam.esm.repository.mapper.GiftCertificateMapper;
import com.epam.esm.repository.mapper.TagIdMapper;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GiftCertificatesDao implements CommonDao<GiftCertificate> {

    private static final String GET_CERTIFICATE_BY_ID_SQL = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date FROM gift_certificates " +
            "WHERE id = ?";
    private static final String GET_ALL_CERTIFICATES_SQL = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date FROM gift_certificates";
    private static final String UPDATE_CERTIFICATE_BY_ID_SQL = "UPDATE gift_certificates " +
            "SET name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ?," +
            "WHERE id = ?";
    private static final String DELETE_CERTIFICATE_BY_ID_SQL = "DELETE FROM gift_certificates WHERE id = ?";
    private static final String CREATE_CERTIFICATE_SQL = "INSERT INTO gift_certificates " +
            "(name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_CERTIFICATE_TAGS_SQL = "SELECT tag_id FROM certificate_tag WHERE certificate_id=?";
    private static final String GET_CERTIFICATES_ID_BY_TAG_SQL =
            "SELECT certificate_id FROM certificate_tag WHERE tag_id=?";

    private final JdbcTemplate jdbcTemplate;
    private TagDao tagDao;

    @Autowired
    public GiftCertificatesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
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
        for (GiftCertificate certificate : certificates) {
            final Set<Tag> tags = fetchCertificateTags(certificate.getId());
            certificate.setTags(tags);
        }
        return certificates;
    }

    @Override
    public boolean create(GiftCertificate entity) {
        jdbcTemplate.update(CREATE_CERTIFICATE_SQL,
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDuration().getDays(),
                entity.getCreateDate(),
                entity.getLastUpdateDate()); //todo add exception handling
        return true;
    }

    @Override
    public GiftCertificate update(GiftCertificate entity) { //todo add exception handling
        final Optional<GiftCertificate> oldCertificateOptional = read(entity.getId());
        if (!oldCertificateOptional.isPresent()) {
            //todo throw exception
            throw new RuntimeException();
        }
        GiftCertificate oldCertificate = oldCertificateOptional.get();
        String name = entity.getName().isEmpty() ? oldCertificate.getName() : entity.getName();
        String description = entity.getDescription().isEmpty()
                ? oldCertificate.getDescription() : entity.getDescription();
        BigDecimal price = entity.getPrice() == null ? oldCertificate.getPrice() : entity.getPrice();
        int duration = entity.getDuration() == null
                ? oldCertificate.getDuration().getDays() : entity.getDuration().getDays();
        LocalDateTime createDate = entity.getCreateDate() == null
                ? oldCertificate.getCreateDate() : entity.getCreateDate();
        LocalDateTime lastUpdateDate = entity.getLastUpdateDate() == null
                ? oldCertificate.getLastUpdateDate() : entity.getLastUpdateDate();
                jdbcTemplate.update(UPDATE_CERTIFICATE_BY_ID_SQL, name, description, price, duration, createDate,
                        lastUpdateDate, entity.getId());
        return oldCertificate;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_CERTIFICATE_BY_ID_SQL, id) > 0;
    }

    public List<GiftCertificate> fetchCertificatesByTagName(String tagName) {
        List<GiftCertificate> certificates = new ArrayList<>();
        final Optional<Tag> tag = tagDao.readByName(tagName);
        if (!tag.isPresent()) {
            return certificates;
        }
        final int tagId = tag.get().getId();
        final List<Integer> certificatesId = jdbcTemplate
                .query(GET_CERTIFICATES_ID_BY_TAG_SQL, new CertificateIdMapper(), tagId);
        for (Integer id : certificatesId) {
            final Optional<GiftCertificate> giftCertificate = read(id);
            giftCertificate.ifPresent(certificates::add);
        }
        return certificates;
    }

    private Set<Tag> fetchCertificateTags(int id) {
        final List<Integer> tagsId = jdbcTemplate.query(GET_CERTIFICATE_TAGS_SQL, new TagIdMapper(), id);
        final Set<Tag> tags = new HashSet<>();
        for (Integer tagId : tagsId) {
            Optional<Tag> tag = tagDao.read(tagId);
            tag.ifPresent(tags::add);
        }
        return tags;
    }
}
