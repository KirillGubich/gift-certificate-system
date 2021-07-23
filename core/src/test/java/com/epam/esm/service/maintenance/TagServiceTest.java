package com.epam.esm.service.maintenance;

import com.epam.esm.repository.config.TestConfig;
import com.epam.esm.repository.dao.TagRepository;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchPageException;
import com.epam.esm.service.exception.NoSuchTagException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestConfig.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ConversionService conversionService;

    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tagService = new TagService(tagRepository, conversionService);
    }

    @Test
    void create_provideObjectToDaoAndReturnCreated_whenCorrect() {
        int id = 1;
        Tag tag = new Tag(id, "AnyName");
        TagDto tagDto = new TagDto(0, "AnyName");
        TagDto createdDto = new TagDto(id, "AnyName");

        when(tagRepository.saveAndFlush(any(Tag.class))).thenReturn(tag);
        when(conversionService.convert(tag, TagDto.class)).thenReturn(createdDto);
        when(conversionService.convert(tagDto, Tag.class)).thenReturn(new Tag(0, "AnyName"));

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

        when(tagRepository.findById(id)).thenReturn(Optional.of(tag));
        when(conversionService.convert(tag, TagDto.class)).thenReturn(tagDto);

        TagDto actual = tagService.read(id);
        assertEquals(tagDto, actual);
    }

    @Test
    void read_throwException_whenTagNotExist() {
        when(tagRepository.findById(anyInt())).thenReturn(Optional.empty());
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

        when(tagRepository.findAll()).thenReturn(tags);
        when(conversionService.convert(tag1, TagDto.class)).thenReturn(tagDto1);
        when(conversionService.convert(tag2, TagDto.class)).thenReturn(tagDto2);

        List<TagDto> actual = tagService.readAll();
        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        int id = 1;
        tagService.delete(id);
    }

    @Test
    void readPaginated_returnListOfTags_whenCorrectParamsProvided() {
        Tag tag1 = new Tag(1, "name1");
        Tag tag2 = new Tag(2, "name2");
        List<Tag> tags = Arrays.asList(tag1, tag2);
        TagDto tagDto1 = new TagDto();
        tagDto1.setId(1);
        tagDto1.setName("name1");
        TagDto tagDto2 = new TagDto(2, "name2");
        int page = 1;
        int size = 2;
        long count = 100;
        List<TagDto> expected = Arrays.asList(tagDto1, tagDto2);
        Page<Tag> tagPage = new PageImpl<>(tags);
        when(tagRepository.findAll(any(PageRequest.class))).thenReturn(tagPage);
        when(tagRepository.count()).thenReturn(count);
        when(conversionService.convert(tag1, TagDto.class)).thenReturn(tagDto1);
        when(conversionService.convert(tag2, TagDto.class)).thenReturn(tagDto2);

        List<TagDto> actual = tagService.readPaginated(page, size);
        assertEquals(expected, actual);
    }

    @Test
    void readPaginated_throwException_whenNoSuchPageExist() {
        long count = 1;
        when(tagRepository.count()).thenReturn(count);
        assertThrows(NoSuchPageException.class,
                () -> tagService.readPaginated(2, 1));
    }

    @Test
    void receiveMostUsedTag_getTag_whenItExist() {
        Tag tag = new Tag(1, "name");
        TagDto expected = new TagDto(1, "name");

        when(tagRepository.findMostWidelyUsedTag()).thenReturn(tag);
        when(conversionService.convert(tag, TagDto.class)).thenReturn(expected);

        TagDto actual = tagService.receiveMostUsedTag();
        assertEquals(expected, actual);
    }

    @Test
    void receiveMostUsedTag_getException_whenNotExist() {
        when(tagRepository.findMostWidelyUsedTag()).thenReturn(null);
        assertThrows(NoSuchTagException.class, () -> tagService.receiveMostUsedTag());
    }

    @Test
    void getLastPage_receiveLastPageNumber_whenItExist() {
        long count = 100;
        int size = 20;
        int expected = 5;
        when(tagRepository.count()).thenReturn(count);
        int actual = tagService.getLastPage(size);
        assertEquals(expected, actual);
    }
}