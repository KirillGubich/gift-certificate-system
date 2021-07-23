package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Order;
import com.epam.esm.service.dto.OrderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderDtoConverterTest {

    private OrderDtoConverter converter;

    @BeforeEach
    void setUp() {
        converter = new OrderDtoConverter();
    }

    @Test
    void convert() {
        int id = 1;
        BigDecimal cost = new BigDecimal("10.56");
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);
        orderDto.setCost(cost);
        Order expected = new Order();
        expected.setId(id);
        expected.setCost(cost);
        Order actual = converter.convert(orderDto);
        assertEquals(expected, actual);
    }
}