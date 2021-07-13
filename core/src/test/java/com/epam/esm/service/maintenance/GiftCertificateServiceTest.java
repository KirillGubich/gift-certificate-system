package com.epam.esm.service.maintenance;

import com.epam.esm.repository.config.TestConfig;
import com.epam.esm.repository.criteria.GiftCertificateCriteria;
import com.epam.esm.repository.dao.GiftCertificateRepository;
import com.epam.esm.repository.dao.TagRepository;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.SortType;
import com.epam.esm.repository.model.SortValue;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchCertificateException;
import com.epam.esm.service.exception.NotExistentUpdateException;
import com.epam.esm.service.validation.GiftCertificateValidator;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestConfig.class)
class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateRepository certificateRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private GiftCertificateValidator validator;

    @Mock
    private ConversionService conversionService;

    private GiftCertificateService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new GiftCertificateService(validator, certificateRepository, tagRepository, conversionService);
    }

    @Test
    void create_receiveCreatedCertificate_whenDtoProvidedWithExistingTags() {
        HashSet<TagDto> tagDtos = new HashSet<>();
        HashSet<Tag> tags = new HashSet<>();
        tagDtos.add(new TagDto(1, "Food"));
        Tag tag = new Tag(1, "Food");
        tags.add(tag);
        LocalDateTime now = LocalDateTime.now();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .withName("TestName")
                .withDescription("Description")
                .withPrice(new BigDecimal("12.56"))
                .withDuration(10)
                .withTags(tagDtos)
                .build();
        GiftCertificate certificate = GiftCertificate.builder()
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
                .withDuration(10)
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(tags)
                .build();
        GiftCertificateDto expected = GiftCertificateDto.builder()
                .withId(1)
                .withName("TestName")
                .withDescription("Description")
                .withPrice(new BigDecimal("12.56"))
                .withDuration(10)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(tagDtos)
                .build();

        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(tag));
        when(certificateRepository.saveAndFlush(any(GiftCertificate.class))).thenReturn(createdCertificate);
        when(conversionService.convert(certificateDto, GiftCertificate.class)).thenReturn(certificate);
        when(conversionService.convert(createdCertificate, GiftCertificateDto.class)).thenReturn(expected);

        GiftCertificateDto actual = service.create(certificateDto);
        assertEquals(expected, actual);
    }

    @Test
    void read_receiveCertificate_whenIdProvidedAndExist() {
        int id = 1;
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate = GiftCertificate.builder()
                .withDuration(10)
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificateDto expected = GiftCertificateDto.builder()
                .withDuration(10)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(new HashSet<>())
                .build();

        when(certificateRepository.findById(id)).thenReturn(Optional.of(certificate));
        when(conversionService.convert(certificate, GiftCertificateDto.class)).thenReturn(expected);

        GiftCertificateDto actual = service.read(id);
        assertEquals(expected, actual);
    }

    @Test
    void read_throwException_whenNotExist() {
        when(certificateRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NoSuchCertificateException.class, () -> service.read(1));
    }

    @Test
    void readAll_receiveAllCertificates_whenRequired() {
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate1 = GiftCertificate.builder()
                .withId(1)
                .withDuration(10)
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificate certificate2 = GiftCertificate.builder()
                .withId(2)
                .withDuration(20)
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

        when(certificateRepository.findAll()).thenReturn(certificates);
        when(conversionService.convert(certificate1, GiftCertificateDto.class)).thenReturn(dto1);
        when(conversionService.convert(certificate2, GiftCertificateDto.class)).thenReturn(dto2);

        List<GiftCertificateDto> expected = Arrays.asList(dto1, dto2);
        List<GiftCertificateDto> actual = service.readAll();
        assertEquals(expected, actual);
    }

    @Test
    void readWithParameters_getListOfCertificates_whenExist() {
        int page = 2;
        int size = 20;
        long count = 100;
        SortType sortType = SortType.ASCENDING;
        SortValue sortValue = SortValue.NAME;
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate1 = GiftCertificate.builder()
                .withId(1)
                .withDuration(10)
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificate certificate2 = GiftCertificate.builder()
                .withId(2)
                .withDuration(20)
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
        Page<GiftCertificate> pageCertificates = new PageImpl<>(certificates);
        when(certificateRepository.findAll(any(PageRequest.class))).thenReturn(pageCertificates);
        when(conversionService.convert(certificate1, GiftCertificateDto.class)).thenReturn(dto1);
        when(conversionService.convert(certificate2, GiftCertificateDto.class)).thenReturn(dto2);
        when(certificateRepository.count()).thenReturn(count);

        List<GiftCertificateDto> expected = Arrays.asList(dto1, dto2);
        List<GiftCertificateDto> actual = service.readWithParameters(page, size, sortValue, sortType);
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
                .withDuration(5)
                .withPrice(new BigDecimal("20.00"))
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificate updatedCertificate = GiftCertificate.builder()
                .withId(id)
                .withName("newName")
                .withDescription("Description")
                .withDuration(10)
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

        when(certificateRepository.findById(id)).thenReturn(Optional.of(oldCertificate));
        when(certificateRepository.saveAndFlush(any(GiftCertificate.class))).thenReturn(updatedCertificate);
        when(validator.validateName(anyString())).thenAnswer(i -> i.getArguments()[0]);
        when(validator.validateDuration(anyInt())).thenAnswer(i -> i.getArguments()[0]);
        when(conversionService.convert(updatedCertificate, GiftCertificateDto.class)).thenReturn(expected);

        GiftCertificateDto actual = service.update(certificateDto);
        assertEquals(expected, actual);
    }

    @Test
    void update_throwException_whenNotExist() {
        when(certificateRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NotExistentUpdateException.class, () -> service.update(GiftCertificateDto.builder().build()));
    }

    @Test
    void delete_returnTrue_whenDeletedSuccessfully() {
        int id = 1;
        service.delete(id);
    }

    @Test
    void searchByCriteria_getListOfCertificates_whenTagsProvided() {
        int page = 2;
        int size = 4;

        GiftCertificateCriteria criteria = GiftCertificateCriteria.builder()
                .withName("ab")
                .withSortValue(SortValue.NAME)
                .withSortType(SortType.DESCENDING)
                .withTagNames(Arrays.asList("Office", "Sport"))
                .build();

        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate1 = GiftCertificate.builder()
                .withId(1)
                .withName("abc")
                .withDuration(10)
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificate certificate2 = GiftCertificate.builder()
                .withId(2)
                .withName("qab")
                .withDuration(20)
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificateDto dto1 = GiftCertificateDto.builder()
                .withId(1)
                .withName("abc")
                .withDuration(10)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(new HashSet<>())
                .build();
        GiftCertificateDto dto2 = GiftCertificateDto.builder()
                .withId(2)
                .withName("qab")
                .withDuration(20)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(new HashSet<>())
                .build();
        List<GiftCertificate> certificates = Arrays.asList(certificate1, certificate2);
        Page<GiftCertificate> certificatePage = new PageImpl<>(certificates);

        when(conversionService.convert(certificate1, GiftCertificateDto.class)).thenReturn(dto1);
        when(conversionService.convert(certificate2, GiftCertificateDto.class)).thenReturn(dto2);
        when(certificateRepository
                .findAllByNameContainsAndDescriptionContainsAndTagsNameIn(anyString(), anyString(),
                        anyCollection(), any(PageRequest.class)))
                .thenReturn(certificatePage);

        List<GiftCertificateDto> expected = Arrays.asList(dto1, dto2);
        List<GiftCertificateDto> actual = service.searchByCriteria(criteria, page, size);
        assertEquals(expected, actual);
    }

    @Test
    void searchByCriteria_getListOfCertificates_whenTagsNotProvided() {
        int page = 2;
        int size = 4;

        GiftCertificateCriteria criteria = GiftCertificateCriteria.builder()
                .withName("ab")
                .withSortValue(SortValue.NAME)
                .withSortType(SortType.DESCENDING)
                .build();

        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate1 = GiftCertificate.builder()
                .withId(1)
                .withName("abc")
                .withDuration(10)
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificate certificate2 = GiftCertificate.builder()
                .withId(2)
                .withName("qab")
                .withDuration(20)
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(new HashSet<>())
                .build();
        GiftCertificateDto dto1 = GiftCertificateDto.builder()
                .withId(1)
                .withName("abc")
                .withDuration(10)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(new HashSet<>())
                .build();
        GiftCertificateDto dto2 = GiftCertificateDto.builder()
                .withId(2)
                .withName("qab")
                .withDuration(20)
                .withCreateDate(now.toString())
                .withLastUpdateDate(now.toString())
                .withTags(new HashSet<>())
                .build();
        List<GiftCertificate> certificates = Arrays.asList(certificate1, certificate2);
        Page<GiftCertificate> certificatePage = new PageImpl<>(certificates);

        when(conversionService.convert(certificate1, GiftCertificateDto.class)).thenReturn(dto1);
        when(conversionService.convert(certificate2, GiftCertificateDto.class)).thenReturn(dto2);
        when(certificateRepository
                .findAllByNameContainsAndDescriptionContains(anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(certificatePage);

        List<GiftCertificateDto> expected = Arrays.asList(dto1, dto2);
        List<GiftCertificateDto> actual = service.searchByCriteria(criteria, page, size);
        assertEquals(expected, actual);
    }
}