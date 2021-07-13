package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.GiftCertificateRepository;
import com.epam.esm.repository.dao.OrderRepository;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GiftCertificateRepository certificateRepository;

    @Mock
    private ConversionService conversionService;

    private OrderService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new OrderService(orderRepository, userRepository, certificateRepository, conversionService);
    }

    @Test
    void read_receiveOrder_whenItExist() {
        int userId = 1;
        int orderId = 2;
        User user = new User(userId, "login", "password", "name", "surname");
        UserDto userDto = new UserDto(userId, "login", "password", "name", "surname");
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
        OrderDto expected = new OrderDto();
        expected.setId(orderId);
        expected.setCost(new BigDecimal("10.3"));
        expected.setPurchaseDate(now.toString());
        expected.setUser(userDto);
        expected.setGiftCertificates(certificateDtos);
        user.setOrders(Collections.singletonList(order));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(conversionService.convert(order, OrderDto.class)).thenReturn(expected);

        OrderDto actual = service.read(orderId);
        assertEquals(expected, actual);
    }

    @Test
    void read_getException_whenNotExist() {
        int id = 1;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchOrderException.class, () -> service.read(id));
    }

    @Test
    void readAll_getListWithOrders_whenTheyExist() {
        int userId = 1;
        int orderId = 2;
        User user = new User(userId, "login", "password", "name", "surname");
        UserDto userDto = new UserDto(userId, "login", "password", "name", "surname");
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

        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
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
        long count = 100;
        User user = new User(userId, "login", "password", "name", "surname");
        UserDto userDto = new UserDto(userId, "login", "password", "name", "surname");
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
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(orderPage);
        when(conversionService.convert(order, OrderDto.class)).thenReturn(orderDto);
        when(orderRepository.count()).thenReturn(count);

        List<OrderDto> actual = service.readPaginated(page, size);
        List<OrderDto> expected = Collections.singletonList(orderDto);
        assertEquals(expected, actual);
    }

    @Test
    void delete_receiveTrue_whenCorrect() {
        int id = 1;
        service.delete(id);
    }

    @Test
    void getLastPage_receiveLastPageNumber_whenItExist() {
        long count = 50;
        int size = 10;
        int expected = 5;
        when(orderRepository.count()).thenReturn(count);
        int actual = service.getLastPage(size);
        assertEquals(expected, actual);
    }
}