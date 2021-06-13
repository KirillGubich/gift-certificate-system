package com.epam.esm.repository.dao;

import com.epam.esm.repository.config.TestConfig;
import com.epam.esm.repository.model.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TagDaoTest {

    @Autowired
    private TagDao dao;

    @Test
    public void testCreate() {
        Tag tag = new Tag(3, "thirdTag");
        assertNotNull(dao.create(tag));
    }

    @Test
    public void testReadById() {
        Optional<Tag> optionalTag = dao.read(1);
        assertTrue(optionalTag.isPresent());
    }

    @Test
    public void testReadByName() {
        Optional<Tag> optionalTag = dao.readByName("firstTag");
        assertTrue(optionalTag.isPresent());
    }

    @Test
    public void testDelete() {
        assertTrue(dao.delete(3));
    }

    @Test
    public void testReadAll() {
        List<Tag> tags = dao.readAll();
        assertTrue(tags.size() > 0);
    }
}