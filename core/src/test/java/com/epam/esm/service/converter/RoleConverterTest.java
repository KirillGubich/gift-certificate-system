package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Role;
import com.epam.esm.service.dto.RoleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleConverterTest {

    private RoleConverter roleConverter;

    @BeforeEach
    void setUp() {
        roleConverter = new RoleConverter();
    }

    @Test
    void convert() {
        Role role = new Role(1, "ADMIN");
        final RoleDto expected = new RoleDto(1, "ADMIN");
        RoleDto actual = roleConverter.convert(role);
        assertEquals(expected, actual);
    }
}