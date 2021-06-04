package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificatesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<GiftCertificate> read(int id) {
        final List<GiftCertificate> queryResult =
                jdbcTemplate.query(GET_CERTIFICATE_BY_ID_SQL, new BeanPropertyRowMapper<>(GiftCertificate.class), id);
        return queryResult.stream().findFirst();
    }

    @Override
    public List<GiftCertificate> readAll() {
        return jdbcTemplate.query(GET_ALL_CERTIFICATES_SQL, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public boolean create(GiftCertificate entity) {
        jdbcTemplate.update(CREATE_CERTIFICATE_SQL); //todo add params, exception handling
        return false;
    }

    @Override
    public GiftCertificate update(GiftCertificate entity) {
        jdbcTemplate.update(UPDATE_CERTIFICATE_BY_ID_SQL); //todo add params, exception handling
        return null;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_CERTIFICATE_BY_ID_SQL, id) > 0;
    }
}
