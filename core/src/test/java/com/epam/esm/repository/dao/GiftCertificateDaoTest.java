package com.epam.esm.repository.dao;

import com.epam.esm.repository.config.TestConfig;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
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
                .withDuration(1)
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
                .withId(1)
                .withName("test1")
                .withDescription("after update")
                .withDuration(1)
                .withPrice(new BigDecimal("50.34"))
                .withCreateDate(LocalDateTime.now())
                .withTags(tags)
                .build();
        GiftCertificate oldCertificate = dao.update(certificate);
        assertNotNull(oldCertificate);
    }

    @Test
    public void testDelete() {
        assertTrue(dao.delete(2));
    }

    @Test
    void fetchCertificatesByTag() {
        List<GiftCertificate> certificates = dao.fetchCertificatesByTag(new Tag(1, "firstTag"));
        assertEquals(2, certificates.size());
    }

    @Test
    void fetchCertificatesByPartOfName() {
        List<GiftCertificate> certificates = dao.fetchCertificatesByPartOfName("test");
        assertEquals(2, certificates.size());
    }

    @Test
    void fetchCertificatesByPartOfDescription() {
        List<GiftCertificate> certificates = dao.fetchCertificatesByPartOfDescription("es");
        assertEquals(1, certificates.size());
    }
}