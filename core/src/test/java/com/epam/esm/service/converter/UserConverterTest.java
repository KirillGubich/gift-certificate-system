package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Role;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    @Mock
    private RoleConverter roleConverter;

    private UserConverter converter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        converter = new UserConverter(roleConverter);
    }

    @Test
    void convert() {
        Set<Role> roles = new HashSet<>();
        Role role = new Role(1, "ADMIN");
        roles.add(role);
        Set<RoleDto> roleDtos = new HashSet<>();
        RoleDto roleDto = new RoleDto(1, "ADMIN");
        roleDtos.add(roleDto);
        User user = new User(1, "login", "Password", "name", "surname");
        user.setRoles(roles);
        UserDto expected = new UserDto(1, "login", "Password",
                "name", "surname", roleDtos);
        Mockito.when(roleConverter.convert(role)).thenReturn(roleDto);
        UserDto actual = converter.convert(user);
        assertEquals(expected, actual);
    }
}