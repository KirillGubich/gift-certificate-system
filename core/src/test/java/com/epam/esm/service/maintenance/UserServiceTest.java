package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.UserRepository;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.Role;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NoSuchUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConversionService conversionService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new UserService(userRepository, conversionService, passwordEncoder);
    }

    @Test
    void read_getUserWithGivenId_whenItExist() {
        int id = 1;
        User user = new User(1, "login", "password", "name", "surname");
        UserDto userDto = new UserDto(1, "login", "password", "name", "surname");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(conversionService.convert(user, UserDto.class)).thenReturn(userDto);

        UserDto actual = service.read(id);
        assertEquals(userDto, actual);
    }

    @Test
    void read_getException_whenNotExist() {
        int id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> service.read(id));
    }

    @Test
    void readAll_getUsersList_whenTheyExist() {
        User user1 = new User(1, "login", "password", "name", "surname");
        User user2 = new User(2, "login2", "password2", "name", "surname");
        UserDto userDto1 = new UserDto(1, "login", "password", "name", "surname");
        UserDto userDto2 = new UserDto();
        userDto2.setId(1);
        userDto2.setLogin("login");
        userDto2.setPassword("password");
        userDto2.setFirstName("name");
        userDto2.setLastName("surname");

        List<User> users = Arrays.asList(user1, user2);
        List<UserDto> expected = Arrays.asList(userDto1, userDto2);

        when(userRepository.findAll()).thenReturn(users);
        when(conversionService.convert(user1, UserDto.class)).thenReturn(userDto1);
        when(conversionService.convert(user2, UserDto.class)).thenReturn(userDto2);

        List<UserDto> actual = service.readAll();
        assertEquals(expected, actual);
    }

    @Test
    void readPaginated_getListOfUsers_whenPageNumberCorrect() {
        int size = 10;
        int page = 5;
        long count = 100;
        User user1 = new User(1, "login", "password", "name", "surname");
        User user2 = new User(2, "login2", "password2", "name", "surname");
        UserDto userDto1 = new UserDto(1, "login", "password", "name", "surname");
        UserDto userDto2 = new UserDto(2, "login2", "password2", "name", "surname");
        List<User> users = Arrays.asList(user1, user2);
        List<UserDto> expected = Arrays.asList(userDto1, userDto2);
        Page<User> userPage = new PageImpl<>(users);

        when(userRepository.count()).thenReturn(count);
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);
        when(conversionService.convert(user1, UserDto.class)).thenReturn(userDto1);
        when(conversionService.convert(user2, UserDto.class)).thenReturn(userDto2);

        List<UserDto> actual = service.readPaginated(page, size);
        assertEquals(expected, actual);
    }

    @Test
    void readUserOrders_getListOfOrders_whenUserExist() {
        int id = 1;
        User user = new User(id, "login", "password", "name", "surname");
        UserDto userDto = new UserDto(id, "login", "password", "name", "surname");
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

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(conversionService.convert(order, OrderDto.class)).thenReturn(orderDto);

        List<OrderDto> actual = service.readUserOrders(id);
        List<OrderDto> expected = Collections.singletonList(orderDto);
        assertEquals(expected, actual);
    }

    @Test
    void getLastPage_receiveLastPageNumber_whenProvidedCorrectData() {
        int size = 20;
        int expected = 5;
        long count = 99;
        when(userRepository.count()).thenReturn(count);
        long actual = service.getLastPage(size);
        assertEquals(expected, actual);
    }

    @Test
    void update_getUpdatedUser_whenCorrectDataProvided() {
        int id = 1;
        String login = "login";
        String password = "password";
        String name = "name";
        String surname = "surname";
        UserDto userDto = new UserDto(id, login, password, name, surname);
        User user = new User(id, login, password, name, surname);
        Set<Role> userRoleSet = new HashSet<>();
        Role userRole = new Role(2, "USER");
        userRoleSet.add(userRole);
        user.setRoles(userRoleSet);

        Set<RoleDto> userRoleDtoSet = new HashSet<>();
        RoleDto userRoleDto = new RoleDto(2, "USER");
        userRoleDtoSet.add(userRoleDto);
        UserDto expected = new UserDto(id, login, password, name, surname);
        expected.setRoles(userRoleDtoSet);

        when(conversionService.convert(userDto, User.class)).thenReturn(user);
        when(conversionService.convert(user, UserDto.class)).thenReturn(expected);
        when(userRepository.saveAndFlush(user)).thenReturn(user);

        UserDto actual = service.update(userDto);
        assertEquals(expected, actual);
    }
}