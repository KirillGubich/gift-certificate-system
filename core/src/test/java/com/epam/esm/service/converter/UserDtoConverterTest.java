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

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoConverterTest {

    @Mock
    private RoleDtoConverter roleDtoConverter;

    private UserDtoConverter converter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        converter = new UserDtoConverter(roleDtoConverter);
    }

    @Test
    void convert() {
        Set<RoleDto> roleDtos = new HashSet<>();
        RoleDto roleDto = new RoleDto(1, "ADMIN");
        roleDtos.add(roleDto);
        Set<Role> roles = new HashSet<>();
        Role role = new Role(1, "ADMIN");
        roles.add(role);
        UserDto userDto = new UserDto(1, "login", "Password", "name",
                "surname");
        userDto.setRoles(roleDtos);
        User expected = new User(1, "login", "Password",
                "name", "surname");
        expected.setRoles(roles);
        Mockito.when(roleDtoConverter.convert(roleDto)).thenReturn(role);
        User actual = converter.convert(userDto);
        assertEquals(expected, actual);
    }
}