package com.epam.esm.repository.mapper;

import com.epam.esm.repository.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Period;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    public GiftCertificateMapper() {
    }

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {

        return GiftCertificate.builder()
                .withId(rs.getInt("id"))
                .withName(rs.getString("name"))
                .withDescription(rs.getString("description"))
                .withPrice(rs.getBigDecimal("price"))
                .withDuration(Period.ofDays(rs.getInt("duration")))
                .withCreateDate(rs.getTimestamp("create_date").toLocalDateTime())
                .withLastUpdateDate(rs.getTimestamp("last_update_date").toLocalDateTime())
                .build();
    }

}
