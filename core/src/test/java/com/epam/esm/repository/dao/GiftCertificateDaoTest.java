package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = GiftCertificateDao.class)
class GiftCertificateDaoTest {

    @Autowired
    private GiftCertificateDao dao;

    @Test
    public void testCreate() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(0, "firstTag"));
        tags.add(new Tag(0, "secondTag"));
        GiftCertificate certificate = GiftCertificate.builder()
                .withName("test3")
                .withDescription("test3")
                .withDuration(Period.ofDays(1))
                .withPrice(BigDecimal.ONE)
                .withTags(tags)
                .build();
        assertNotNull(dao.create(certificate));
    }

    @Test
    public void testRead() {
        Optional<GiftCertificate> certificateOptional = dao.read(2);
        assertTrue(certificateOptional.isPresent());

    }

    @Test
    public void testReadAll() {
        List<GiftCertificate> certificates = dao.readAll();
        assertEquals(2, certificates.size());
    }

    @Test
    public void testUpdate() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(0, "firstTag"));
        GiftCertificate certificate = GiftCertificate.builder()
                .withName("test1")
                .withDescription("after update")
                .withDuration(Period.ofDays(1))
                .withPrice(new BigDecimal("50.34"))
                .withTags(tags)
                .build();
        GiftCertificate oldCertificate = dao.update(certificate);
        assertNotNull(oldCertificate);
    }

    @Test
    public void testDelete() {
        assertTrue(dao.delete(3));
    }

    @Test
    void fetchCertificatesByTagId() {
        List<GiftCertificate> certificates = dao.fetchCertificatesByTagId(1);
        assertEquals(2, certificates.size());
    }

    @Test
    void fetchCertificatesByPartOfName() {
        List<GiftCertificate> certificates = dao.fetchCertificatesByPartOfName("test");
        assertEquals(2, certificates.size());
    }

    @Test
    void fetchCertificatesByPartOfDescription() {
        List<GiftCertificate> certificates = dao.fetchCertificatesByPartOfDescription("desc");
        assertEquals(2, certificates.size());
    }

    @Test
    void addTagToCertificate() {
        dao.addTagToCertificate(2, 2);
        Optional<GiftCertificate> giftCertificate = dao.read(2);
        boolean actual = false;
        if (giftCertificate.isPresent()) {
            actual = giftCertificate.get().getTags().contains(new Tag(2, "secondTag"));
        }
        assertTrue(actual);
    }

    @Test
    void removeTagFromCertificate() {
        dao.addTagToCertificate(2, 1);
        Optional<GiftCertificate> giftCertificate = dao.read(2);
        boolean actual = true;
        if (giftCertificate.isPresent()) {
            actual = giftCertificate.get().getTags().contains(new Tag(1, "firstTag"));
        }
        assertFalse(actual);
    }
}