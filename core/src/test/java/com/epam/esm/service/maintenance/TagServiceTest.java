package com.epam.esm.service.maintenance;

import com.epam.esm.repository.config.TestConfig;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.converter.TagDtoConverter;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchPageException;
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
    private TagDao tagDao;

    @Mock
    private TagConverter tagConverter;

    @Mock
    private TagDtoConverter tagDtoConverter;

    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tagService = new TagService(tagDao, tagConverter, tagDtoConverter);
    }

    @Test
    void create_provideObjectToDaoAndReturnCreated_whenCorrect() {
        int id = 1;
        Tag tag = new Tag(id, "AnyName");
        TagDto tagDto = new TagDto(0, "AnyName");
        TagDto createdDto = new TagDto(id, "AnyName");

        when(tagDao.create(any(Tag.class))).thenReturn(tag);
        when(tagConverter.convert(tag)).thenReturn(createdDto);
        when(tagDtoConverter.convert(any(TagDto.class))).thenReturn(new Tag(0, "AnyName"));

        TagDto actual = tagService.create(tagDto);
        assertEquals(createdDto, actual);
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
        TagDto tagDto = new TagDto(id, "AnyName");

        when(tagDao.read(id)).thenReturn(Optional.of(tag));
        when(tagConverter.convert(tag)).thenReturn(tagDto);

        TagDto actual = tagService.read(id);
        assertEquals(tagDto, actual);
    }

    @Test
    void read_throwException_whenTagNotExist() {
        when(tagDao.read(anyInt())).thenReturn(Optional.empty());
        assertThrows(NoSuchTagException.class,
                () -> tagService.read(1));
    }

    @Test
    void readAll_returnAllTags_whenRequired() {
        Tag tag1 = new Tag(1, "name1");
        Tag tag2 = new Tag(2, "name2");
        List<Tag> tags = Arrays.asList(tag1, tag2);
        TagDto tagDto1 = new TagDto(1, "name1");
        TagDto tagDto2 = new TagDto(2, "name2");
        List<TagDto> expected = Arrays.asList(tagDto1, tagDto2);

        when(tagDao.readAll()).thenReturn(tags);
        when(tagConverter.convert(tag1)).thenReturn(tagDto1);
        when(tagConverter.convert(tag2)).thenReturn(tagDto2);

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

    @Test
    void readPaginated_returnListOfTags_whenCorrectParamsProvided() {
        Tag tag1 = new Tag(1, "name1");
        Tag tag2 = new Tag(2, "name2");
        List<Tag> tags = Arrays.asList(tag1, tag2);
        TagDto tagDto1 = new TagDto(1, "name1");
        TagDto tagDto2 = new TagDto(2, "name2");
        int page = 1;
        int size = 2;
        List<TagDto> expected = Arrays.asList(tagDto1, tagDto2);

        when(tagDao.readPaginated(page, size)).thenReturn(tags);
        when(tagConverter.convert(tag1)).thenReturn(tagDto1);
        when(tagConverter.convert(tag2)).thenReturn(tagDto2);
        when(tagDao.fetchNumberOfPages(size)).thenReturn(1);

        List<TagDto> actual = tagService.readPaginated(page, size);
        assertEquals(expected, actual);
    }

    @Test
    void readPaginated_throwException_whenNoSuchPageExist() {
        when(tagDao.fetchNumberOfPages(anyInt())).thenReturn(1);
        assertThrows(NoSuchPageException.class,
                () -> tagService.readPaginated(2, 1));
    }

    @Test
    void receiveMostUsedTag_getTag_whenItExist() {
        Tag tag = new Tag(1, "name");
        TagDto expected = new TagDto(1, "name");

        when(tagDao.readMostWidelyUsedTag()).thenReturn(Optional.of(tag));
        when(tagConverter.convert(tag)).thenReturn(expected);

        TagDto actual = tagService.receiveMostUsedTag();
        assertEquals(expected, actual);
    }

    @Test
    void receiveMostUsedTag_getException_whenNotExist() {
        when(tagDao.readMostWidelyUsedTag()).thenReturn(Optional.empty());
        assertThrows(NoSuchTagException.class, () -> tagService.receiveMostUsedTag());
    }

    @Test
    void getLastPage_receiveLastPageNumber_whenItExist() {
        int size = 20;
        int expected = 5;
        when(tagDao.fetchNumberOfPages(size)).thenReturn(expected);
        int actual = tagService.getLastPage(size);
        assertEquals(expected, actual);
    }
}