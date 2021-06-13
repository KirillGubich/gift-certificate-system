package com.epam.esm.service.maintenance;

import com.epam.esm.repository.config.TestConfig;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchCertificateException;
import com.epam.esm.service.exception.NotExistentUpdateException;
import com.epam.esm.service.validation.GiftCertificateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestConfig.class)
class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateDao certificateDao;

    @Mock
    private TagDao tagDao;

    @Mock
    private GiftCertificateValidator validator;

    @InjectMocks
    private GiftCertificateService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_receiveCreatedCertificate_whenDtoProvidedWithExistingTags() {
        HashSet<TagDto> tags = new HashSet<>();
        tags.add(new TagDto(1, "Food"));
        Tag tag = new Tag(1, "Food");
        LocalDateTime now = LocalDateTime.now();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .withId(1)
                .withName("TestName")
                .withDescription("Description")
                .withPrice(new BigDecimal("12.56"))
                .withDuration(10)
                .withTags(tags)
                .build();
        GiftCertificate createdCertificate = GiftCertificate.builder()
                .withId(1)
                .withName("TestName")
                .withDescription("Description")
                .withPrice(new BigDecimal("12.56"))
                .withDuration(Period.ofDays(10))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificateDto expected = GiftCertificateDto.builder()
                .withId(1)
                .withName("TestName")
                .withDescription("Description")
                .withPrice(new BigDecimal("12.56"))
                .withDuration(10)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(tags)
                .build();
        when(tagDao.readByName(anyString())).thenReturn(Optional.of(tag));
        when(certificateDao.create(any(GiftCertificate.class))).thenReturn(createdCertificate);
        GiftCertificateDto actual = service.create(certificateDto);
        assertEquals(expected, actual);
    }

    @Test
    void read_receiveCertificate_whenIdProvidedAndExist() {
        int id = 1;
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate = GiftCertificate.builder()
                .withDuration(Period.ofDays(10))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        when(certificateDao.read(id)).thenReturn(Optional.of(certificate));
        GiftCertificateDto expected = GiftCertificateDto.builder()
                .withDuration(10)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(new HashSet<>())
                .build();
        GiftCertificateDto actual = service.read(id);
        assertEquals(expected, actual);
    }

    @Test
    void read_throwException_whenNotExist() {
        when( certificateDao.read(anyInt())).thenReturn(Optional.empty());
        assertThrows(NoSuchCertificateException.class, () -> service.read(1));
    }

    @Test
    void readAll_receiveAllCertificates_whenRequired() {
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate1 = GiftCertificate.builder()
                .withId(1)
                .withDuration(Period.ofDays(10))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificate certificate2 = GiftCertificate.builder()
                .withId(2)
                .withDuration(Period.ofDays(20))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificateDto dto1 = GiftCertificateDto.builder()
                .withId(1)
                .withDuration(10)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(new HashSet<>())
                .build();
        GiftCertificateDto dto2 = GiftCertificateDto.builder()
                .withId(2)
                .withDuration(20)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(new HashSet<>())
                .build();
        List<GiftCertificate> certificates = Arrays.asList(certificate1, certificate2);
        when(certificateDao.readAll()).thenReturn(certificates);
        List<GiftCertificateDto> expected = Arrays.asList(dto1, dto2);
        List<GiftCertificateDto> actual = service.readAll();
        assertEquals(expected, actual);
    }

    @Test
    void update_receiveUpdatedCertificate_whenCorrectDataProvided() {
        LocalDateTime now = LocalDateTime.now();
        int id = 1;
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .withId(id)
                .withName("newName")
                .withDuration(10)
                .build();
        GiftCertificate oldCertificate = GiftCertificate.builder()
                .withId(id)
                .withName("oldName")
                .withDescription("Description")
                .withDuration(Period.ofDays(5))
                .withPrice(new BigDecimal("20.00"))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificate updatedCertificate = GiftCertificate.builder()
                .withId(id)
                .withName("newName")
                .withDescription("Description")
                .withDuration(Period.ofDays(10))
                .withPrice(new BigDecimal("20.00"))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificateDto expected = GiftCertificateDto.builder()
                .withId(id)
                .withName("newName")
                .withDescription("Description")
                .withDuration(10)
                .withPrice(new BigDecimal("20.00"))
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(new HashSet<>())
                .build();
        when(certificateDao.read(id)).thenReturn(Optional.of(oldCertificate));
        when(certificateDao.update(any(GiftCertificate.class))).thenReturn(updatedCertificate);
        when(validator.validateName(anyString())).thenAnswer(i -> i.getArguments()[0]);
        when(validator.validateDescription(anyString())).thenAnswer(i -> i.getArguments()[0]);
        when(validator.validatePrice(any(BigDecimal.class))).thenAnswer(i -> i.getArguments()[0]);
        when(validator.validateDuration(anyInt())).thenAnswer(i -> i.getArguments()[0]);
        GiftCertificateDto actual = service.update(certificateDto);
        assertEquals(expected, actual);
    }

    @Test
    void update_throwException_whenNotExist() {
        when(certificateDao.read(anyInt())).thenReturn(Optional.empty());
        assertThrows(NotExistentUpdateException.class, () -> service.update(GiftCertificateDto.builder().build()));
    }

    @Test
    void delete_returnTrue_whenDeletedSuccessfully() {
        int id = 1;
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate = GiftCertificate.builder()
                .withId(1)
                .withDuration(Period.ofDays(10))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        when(certificateDao.read(id)).thenReturn(Optional.of(certificate));
        when(certificateDao.delete(id)).thenReturn(true);
        boolean actual = service.delete(id);
        assertTrue(actual);
    }

    @Test
    void searchByTagName_receiveListOfCertificates_whenTagExist() {
        String tagName = "AnyName";
        int tagId = 1;
        Tag tag = new Tag(tagId, tagName);
        Set<Tag> tags = new HashSet<>();
        tags.add(tag);
        Set<TagDto> tagDtos = new HashSet<>();
        tagDtos.add(new TagDto(tagId, tagName));
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate1 = GiftCertificate.builder()
                .withId(1)
                .withName("test1")
                .withDuration(Period.ofDays(10))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(tags)
                .build();
        GiftCertificate certificate2 = GiftCertificate.builder()
                .withId(2)
                .withName("test2")
                .withDuration(Period.ofDays(20))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(tags)
                .build();
        GiftCertificateDto dto1 = GiftCertificateDto.builder()
                .withId(1)
                .withName("test1")
                .withDuration(10)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(tagDtos)
                .build();
        GiftCertificateDto dto2 = GiftCertificateDto.builder()
                .withId(2)
                .withName("test2")
                .withDuration(20)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(tagDtos)
                .build();
        List<GiftCertificate> certificates = Arrays.asList(certificate1, certificate2);
        List<GiftCertificateDto> expected = Arrays.asList(dto1, dto2);
        when(tagDao.readByName(tagName)).thenReturn(Optional.of(tag));
        when(certificateDao.fetchCertificatesByTagId(tagId)).thenReturn(certificates);
        List<GiftCertificateDto> actual = service.searchByTagName(tagName);
        assertEquals(expected, actual);
    }

    @Test
    void searchByTagName_receiveEmptyList_whenTagNotExist() {
        String tagName = "AnyName";
        when(tagDao.readByName(tagName)).thenReturn(Optional.empty());
        List<GiftCertificateDto> actual = service.searchByTagName(tagName);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void searchByPartOfName_receiveListOfCertificates_whenPartOfNameCorrect() {
        String partOfDescription = "sale";
        Set<Tag> tags = new HashSet<>();
        Set<TagDto> tagDtos = new HashSet<>();
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate1 = GiftCertificate.builder()
                .withId(1)
                .withName("test1")
                .withDescription("sale 20%")
                .withDuration(Period.ofDays(10))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(tags)
                .build();
        GiftCertificate certificate2 = GiftCertificate.builder()
                .withId(2)
                .withName("test2")
                .withDescription("sale 30%")
                .withDuration(Period.ofDays(20))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(tags)
                .build();
        GiftCertificateDto dto1 = GiftCertificateDto.builder()
                .withId(1)
                .withName("test1")
                .withDescription("sale 20%")
                .withDuration(10)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(tagDtos)
                .build();
        GiftCertificateDto dto2 = GiftCertificateDto.builder()
                .withId(2)
                .withName("test2")
                .withDescription("sale 30%")
                .withDuration(20)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(tagDtos)
                .build();
        List<GiftCertificate> certificates = Arrays.asList(certificate1, certificate2);
        List<GiftCertificateDto> expected = Arrays.asList(dto1, dto2);
        when(certificateDao.fetchCertificatesByPartOfDescription(partOfDescription)).thenReturn(certificates);
        List<GiftCertificateDto> actual = service.searchByPartOfDescription(partOfDescription);
        assertEquals(expected, actual);
    }

    @Test
    void searchByPartOfDescription_receiveListOfCertificates_whenCorrectListProvided() {
        String partOfName = "est";
        String tagName = "AnyName";
        int tagId = 1;
        Tag tag = new Tag(tagId, tagName);
        Set<Tag> tags = new HashSet<>();
        tags.add(tag);
        Set<TagDto> tagDtos = new HashSet<>();
        tagDtos.add(new TagDto(tagId, tagName));
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate1 = GiftCertificate.builder()
                .withId(1)
                .withName("test1")
                .withDuration(Period.ofDays(10))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(tags)
                .build();
        GiftCertificate certificate2 = GiftCertificate.builder()
                .withId(2)
                .withName("test2")
                .withDuration(Period.ofDays(20))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(tags)
                .build();
        GiftCertificateDto dto1 = GiftCertificateDto.builder()
                .withId(1)
                .withName("test1")
                .withDuration(10)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(tagDtos)
                .build();
        GiftCertificateDto dto2 = GiftCertificateDto.builder()
                .withId(2)
                .withName("test2")
                .withDuration(20)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(tagDtos)
                .build();
        List<GiftCertificate> certificates = Arrays.asList(certificate1, certificate2);
        List<GiftCertificateDto> expected = Arrays.asList(dto1, dto2);
        when(certificateDao.fetchCertificatesByPartOfName(partOfName)).thenReturn(certificates);
        List<GiftCertificateDto> actual = service.searchByPartOfName(partOfName);
        assertEquals(expected, actual);
    }

    @Test
    void sortByNameAscending_sortCorrect_whenCorrectListProvided() {
        List<GiftCertificateDto> certificates = Arrays
                .asList(GiftCertificateDto.builder().withName("Sport").build(),
                        GiftCertificateDto.builder().withName("Food").build(),
                        GiftCertificateDto.builder().withName("Relax").build());
        List<GiftCertificateDto> expected = Arrays
                .asList(GiftCertificateDto.builder().withName("Food").build(),
                        GiftCertificateDto.builder().withName("Relax").build(),
                        GiftCertificateDto.builder().withName("Sport").build());
        service.sortByNameAscending(certificates);
        assertEquals(expected, certificates);
    }

    @Test
    void sortByNameDescending_sortCorrect_whenCorrectListProvided() {
        List<GiftCertificateDto> certificates = Arrays
                .asList(GiftCertificateDto.builder().withName("Sport").build(),
                        GiftCertificateDto.builder().withName("Food").build(),
                        GiftCertificateDto.builder().withName("Relax").build());
        List<GiftCertificateDto> expected = Arrays
                .asList(GiftCertificateDto.builder().withName("Sport").build(),
                        GiftCertificateDto.builder().withName("Relax").build(),
                        GiftCertificateDto.builder().withName("Food").build());
        service.sortByNameDescending(certificates);
        assertEquals(expected, certificates);
    }

    @Test
    void sortByDateAscending_sortCorrect_whenCorrectListProvided() {
        List<GiftCertificateDto> certificates = Arrays.asList(
                GiftCertificateDto.builder().withCreateDate("2021-05-29T07:36:14.576").build(),
                GiftCertificateDto.builder().withCreateDate("2021-03-23T16:17:11.112").build(),
                GiftCertificateDto.builder().withCreateDate("2021-04-19T08:22:45.235").build());
        List<GiftCertificateDto> expected = Arrays.asList(
                GiftCertificateDto.builder().withCreateDate("2021-03-23T16:17:11.112").build(),
                GiftCertificateDto.builder().withCreateDate("2021-04-19T08:22:45.235").build(),
                GiftCertificateDto.builder().withCreateDate("2021-05-29T07:36:14.576").build());
        service.sortByDateAscending(certificates);
        assertEquals(expected, certificates);
    }

    @Test
    void sortByDateDescending_sortCorrect_whenCorrectListProvided() {
        List<GiftCertificateDto> certificates = Arrays.asList(
                GiftCertificateDto.builder().withCreateDate("2021-05-29T07:36:14.576").build(),
                GiftCertificateDto.builder().withCreateDate("2021-03-23T16:17:11.112").build(),
                GiftCertificateDto.builder().withCreateDate("2021-04-19T08:22:45.235").build());
        List<GiftCertificateDto> expected = Arrays.asList(
                GiftCertificateDto.builder().withCreateDate("2021-05-29T07:36:14.576").build(),
                GiftCertificateDto.builder().withCreateDate("2021-04-19T08:22:45.235").build(),
                GiftCertificateDto.builder().withCreateDate("2021-03-23T16:17:11.112").build());
        service.sortByDateDescending(certificates);
        assertEquals(expected, certificates);
    }
}