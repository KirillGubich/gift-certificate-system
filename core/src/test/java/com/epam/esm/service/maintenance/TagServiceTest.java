package com.epam.esm.service.maintenance;

import com.epam.esm.repository.config.TestConfig;
import com.epam.esm.repository.dao.CommonDao;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchTagException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestConfig.class)
class TagServiceTest {

    @Mock
    private CommonDao<Tag> tagDao;

    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tagService = new TagService(tagDao);
    }

    @Test
    void create_provideObjectToDaoAndReturnCreated_whenCorrect() {
        int id = 1;
        Tag tag = new Tag(id, "AnyName");
        TagDto tagDto = new TagDto(0, "AnyName");
        when(tagDao.create(any(Tag.class))).thenReturn(tag);
        TagDto actual = tagService.create(tagDto);
        TagDto expected = new TagDto(id, "AnyName");
        assertEquals(expected, actual);
    }

    @Test
    void create_throwException_whenParameterNull() {
        assertThrows(IllegalArgumentException.class,
                () -> tagService.create(null));
    }

    @Test
    void read_returnTag_whenIdProvided() {
        int id = 1;
        Tag tag = new Tag(id, "AnyName");
        when(tagDao.read(id)).thenReturn(Optional.of(tag));
        TagDto actual = tagService.read(id);
        TagDto expected = new TagDto(id, "AnyName");
        assertEquals(expected, actual);
    }

    @Test
    void read_throwException_whenTagNotExist() {
        when(tagDao.read(anyInt())).thenReturn(Optional.empty());
        assertThrows(NoSuchTagException.class,
                () -> tagService.read(1));
    }

    @Test
    void readAll_returnAllTags_whenRequired() {
        List<Tag> tags = Arrays.asList(new Tag(1, "name1"), new Tag(2, "name2"));
        List<TagDto> expected = Arrays.asList(new TagDto(1, "name1"), new TagDto(2, "name2"));
        when(tagDao.readAll()).thenReturn(tags);
        List<TagDto> actual = tagService.readAll();
        assertEquals(expected, actual);
    }

    @Test
    void delete_returnTrue_whenDeletedSuccessfully() {
        int id = 1;
        when(tagDao.delete(id)).thenReturn(true);
        boolean actual = tagService.delete(id);
        assertTrue(actual);
    }

    @Test
    void delete_returnFalse_whenSomethingWentWrong() {
        int id = 1;
        when(tagDao.delete(id)).thenReturn(false);
        boolean actual = tagService.delete(id);
        assertFalse(actual);
    }
}