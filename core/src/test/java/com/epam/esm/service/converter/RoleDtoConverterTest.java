package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Role;
import com.epam.esm.service.dto.RoleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleDtoConverterTest {

    private RoleDtoConverter roleDtoConverter;

    @BeforeEach
    void setUp() {
        roleDtoConverter = new RoleDtoConverter();
    }

    @Test
    void convert() {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1);
        roleDto.setName("ADMIN");
        Role expected = new Role(1, "ADMIN");
        final Role actual = roleDtoConverter.convert(roleDto);
        assertEquals(expected, actual);
    }
}