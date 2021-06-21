package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Order;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter implements Converter<Order, OrderDto> {

    private final UserConverter userConverter;
    private final GiftCertificateConverter giftCertificateConverter;

    @Autowired
    public OrderConverter(UserConverter userConverter, GiftCertificateConverter giftCertificateConverter) {
        this.userConverter = userConverter;
        this.giftCertificateConverter = giftCertificateConverter;
    }

    @Override
    public OrderDto convert(Order source) {
        UserDto userDto = userConverter.convert(source.getUser());
        List<GiftCertificateDto> certificatesDto = source.getGiftCertificates().stream()
                .map(giftCertificateConverter::convert)
                .collect(Collectors.toList());
        return new OrderDto(source.getId(),
                source.getCost(),
                source.getPurchaseDate().toString(),
                userDto,
                certificatesDto);
    }
}
