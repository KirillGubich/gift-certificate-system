package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Order;
import com.epam.esm.service.dto.OrderDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class OrderDtoConverter implements Converter<OrderDto, Order> {

    @Override
    public Order convert(OrderDto source) {
        Order order = new Order();
        order.setId(source.getId());
        order.setCost(source.getCost());
        return order;
    }
}
