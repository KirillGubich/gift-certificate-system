package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NoSuchUserException;
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
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private ConversionService conversionService;

    private UserService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new UserService(null, userDao, conversionService);
    }

    @Test
    void read_getUserWithGivenId_whenItExist() {
        int id = 1;
        User user = new User(1, "name", "password");
        UserDto userDto = new UserDto(1, "name", "password");

        when(userDao.read(id)).thenReturn(Optional.of(user));
        when(conversionService.convert(user, UserDto.class)).thenReturn(userDto);

        UserDto actual = service.read(id);
        assertEquals(userDto, actual);
    }

    @Test
    void read_getException_whenNotExist() {
        int id = 1;
        when(userDao.read(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> service.read(id));
    }

    @Test
    void readAll_getUsersList_whenTheyExist() {
        User user1 = new User(1, "name", "password");
        User user2 = new User(1, "name2", "password2");
        UserDto userDto1 = new UserDto(1, "name", "password");
        UserDto userDto2 = new UserDto(1, "name2", "password2");
        List<User> users = Arrays.asList(user1, user2);
        List<UserDto> expected = Arrays.asList(userDto1, userDto2);

        when(userDao.readAll()).thenReturn(users);
        when(conversionService.convert(user1, UserDto.class)).thenReturn(userDto1);
        when(conversionService.convert(user2, UserDto.class)).thenReturn(userDto2);

        List<UserDto> actual = service.readAll();
        assertEquals(expected, actual);
    }

    @Test
    void readPaginated_getListOfUsers_whenPageNumberCorrect() {
        int size = 10;
        int page = 5;
        User user1 = new User(1, "name", "password");
        User user2 = new User(1, "name2", "password2");
        UserDto userDto1 = new UserDto(1, "name", "password");
        UserDto userDto2 = new UserDto(1, "name2", "password2");
        List<User> users = Arrays.asList(user1, user2);
        List<UserDto> expected = Arrays.asList(userDto1, userDto2);

        when(userDao.fetchNumberOfPages(size)).thenReturn(page + 1);
        when(userDao.readPaginated(page, size)).thenReturn(users);
        when(conversionService.convert(user1, UserDto.class)).thenReturn(userDto1);
        when(conversionService.convert(user2, UserDto.class)).thenReturn(userDto2);

        List<UserDto> actual = service.readPaginated(page, size);
        assertEquals(expected, actual);
    }

    @Test
    void readUserOrders_getListOfOrders_whenUserExist() {
        int id = 1;
        User user = new User(id, "name", "password");
        UserDto userDto = new UserDto(id, "name", "password");
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
        Order order = new Order(1, new BigDecimal("10.34"), now, user, certificates);
        OrderDto orderDto = new OrderDto(1, new BigDecimal("10.34"), now.toString(), userDto, certificateDtos);
        user.setOrders(Collections.singletonList(order));

        when(userDao.read(id)).thenReturn(Optional.of(user));
        when(conversionService.convert(order, OrderDto.class)).thenReturn(orderDto);

        List<OrderDto> actual = service.readUserOrders(id);
        List<OrderDto> expected = Collections.singletonList(orderDto);
        assertEquals(expected, actual);
    }

    @Test
    void getLastPage_receiveLastPageNumber_whenProvidedCorrectData() {
        int size = 20;
        int expected = 5;
        when(userDao.fetchNumberOfPages(size)).thenReturn(expected);
        int actual = service.getLastPage(size);
        assertEquals(expected, actual);
    }
}