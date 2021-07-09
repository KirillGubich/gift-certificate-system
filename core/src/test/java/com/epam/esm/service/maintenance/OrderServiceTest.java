package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.UserRepository;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NoSuchOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GiftCertificateDao certificateDao;

    @Mock
    private ConversionService conversionService;

    private OrderService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new OrderService(orderDao, userRepository, certificateDao, conversionService);
    }

    @Test
    void create_getUpdatedOrder_whenCorrectDataProvided() {
        int userId = 1;
        int orderId = 2;
        User user = new User(userId, "name", "password");
        UserDto userDto = new UserDto(userId, "name", "password");
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
        List<GiftCertificateDto> certificateDtos = Arrays.asList(dto1, dto2);
        Order order = new Order(0, new BigDecimal("10.3"), now, user, certificates);
        Order createdOrder = new Order(orderId, new BigDecimal("10.3"), now, user, certificates);
        OrderDto orderDto = new OrderDto(0, new BigDecimal("10.3"), now.toString(), userDto, certificateDtos);
        OrderDto expected = new OrderDto(orderId, new BigDecimal("10.3"), now.toString(), userDto, certificateDtos);

        when(conversionService.convert(orderDto, Order.class)).thenReturn(order);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(certificateDao.read(dto1.getId())).thenReturn(Optional.of(certificate1));
        when(certificateDao.read(dto2.getId())).thenReturn(Optional.of(certificate2));
        when(conversionService.convert(createdOrder, OrderDto.class)).thenReturn(expected);
        when(orderDao.create(order)).thenReturn(createdOrder);

        OrderDto actual = service.create(orderDto);
        assertEquals(expected, actual);
    }

    @Test
    void read_receiveOrder_whenItExist() {
        int userId = 1;
        int orderId = 2;
        User user = new User(userId, "name", "password");
        UserDto userDto = new UserDto(userId, "name", "password");
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
        List<GiftCertificateDto> certificateDtos = Arrays.asList(dto1, dto2);
        Order order = new Order(orderId, new BigDecimal("10.3"), now, user, certificates);
        OrderDto expected = new OrderDto(orderId, new BigDecimal("10.3"), now.toString(), userDto, certificateDtos);
        user.setOrders(Collections.singletonList(order));

        when(orderDao.read(orderId)).thenReturn(Optional.of(order));
        when(conversionService.convert(order, OrderDto.class)).thenReturn(expected);

        OrderDto actual = service.read(orderId);
        assertEquals(expected, actual);
    }

    @Test
    void read_getException_whenNotExist() {
        int id = 1;
        when(orderDao.read(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchOrderException.class, () -> service.read(id));
    }

    @Test
    void readAll_getListWithOrders_whenTheyExist() {
        int userId = 1;
        int orderId = 2;
        User user = new User(userId, "name", "password");
        UserDto userDto = new UserDto(userId, "name", "password");
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
        List<GiftCertificateDto> certificateDtos = Arrays.asList(dto1, dto2);
        Order order = new Order(orderId, new BigDecimal("10.3"), now, user, certificates);
        OrderDto orderDto = new OrderDto(orderId, new BigDecimal("10.3"), now.toString(), userDto, certificateDtos);
        user.setOrders(Collections.singletonList(order));

        when(orderDao.readAll()).thenReturn(Collections.singletonList(order));
        when(conversionService.convert(order, OrderDto.class)).thenReturn(orderDto);

        List<OrderDto> actual = service.readAll();
        List<OrderDto> expected = Collections.singletonList(orderDto);
        assertEquals(expected, actual);
    }

    @Test
    void readPaginated_getListWithOrders_whenTheyExist() {
        int userId = 1;
        int orderId = 2;
        int page = 5;
        int size = 10;
        User user = new User(userId, "name", "password");
        UserDto userDto = new UserDto(userId, "name", "password");
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
        List<GiftCertificateDto> certificateDtos = Arrays.asList(dto1, dto2);
        Order order = new Order(orderId, new BigDecimal("10.3"), now, user, certificates);
        OrderDto orderDto = new OrderDto(orderId, new BigDecimal("10.3"), now.toString(), userDto, certificateDtos);
        user.setOrders(Collections.singletonList(order));

        when(orderDao.readPaginated(page, size)).thenReturn(Collections.singletonList(order));
        when(conversionService.convert(order, OrderDto.class)).thenReturn(orderDto);
        when(orderDao.fetchNumberOfPages(size)).thenReturn(page + 1);

        List<OrderDto> actual = service.readPaginated(page, size);
        List<OrderDto> expected = Collections.singletonList(orderDto);
        assertEquals(expected, actual);
    }

    @Test
    void update_getUpdatedEntity_whenCorrectDataProvided() {
        int userId = 1;
        int orderId = 2;
        User user = new User(userId, "name", "password");
        UserDto userDto = new UserDto(userId, "name", "password");
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
        List<GiftCertificateDto> certificateDtos = Arrays.asList(dto1, dto2);
        OrderDto orderDto = new OrderDto(orderId, new BigDecimal("20.3"), now.toString(), userDto, certificateDtos);
        Order order = new Order(orderId, new BigDecimal("10.3"), now, user, certificates);
        OrderDto expected = new OrderDto(orderId, new BigDecimal("20.3"), now.toString(), userDto, certificateDtos);

        when(orderDao.read(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(certificateDao.read(dto1.getId())).thenReturn(Optional.of(certificate1));
        when(certificateDao.read(dto2.getId())).thenReturn(Optional.of(certificate2));
        when(orderDao.update(order)).thenReturn(order);
        when(conversionService.convert(order, OrderDto.class)).thenReturn(expected);

        OrderDto actual = service.update(orderDto);
        assertEquals(expected, actual);
    }

    @Test
    void delete_receiveTrue_whenCorrect() {
        int id = 1;
        when(orderDao.delete(id)).thenReturn(true);
        boolean actual = service.delete(id);
        assertTrue(actual);
    }

    @Test
    void getLastPage_receiveLastPageNumber_whenItExist() {
        int size = 10;
        int expected = 5;
        when(orderDao.fetchNumberOfPages(size)).thenReturn(expected);
        int actual = service.getLastPage(size);
        assertEquals(expected, actual);
    }
}