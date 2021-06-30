package com.epam.esm.service.converter;

import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    private UserConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UserConverter();
    }

    @Test
    void convert() {
        User user = new User(1, "name", "Password");
        UserDto expected = new UserDto(1, "name", "Password");
        UserDto actual = converter.convert(user);
        assertEquals(expected, actual);
    }
}