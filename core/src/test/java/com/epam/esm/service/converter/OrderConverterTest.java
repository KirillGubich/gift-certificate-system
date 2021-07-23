package com.epam.esm.service.converter;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class OrderConverterTest {

    @Mock
    private UserConverter userConverter;

    @Mock
    private GiftCertificateConverter giftCertificateConverter;

    private OrderConverter converter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        converter = new OrderConverter(userConverter, giftCertificateConverter);
    }

    @Test
    void convert() {
        int userId = 1;
        int orderId = 2;
        User user = new User(userId, "name", "password");
        UserDto userDto = new UserDto(userId, "name", "password");
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
        Order order = new Order(orderId, new BigDecimal("10.3"), now, user, certificates);
        OrderDto expected = new OrderDto(orderId, new BigDecimal("10.3"), now.toString(), userDto, certificateDtos);

        when(userConverter.convert(user)).thenReturn(userDto);
        when(giftCertificateConverter.convert(certificate1)).thenReturn(dto1);
        when(giftCertificateConverter.convert(certificate2)).thenReturn(dto2);

        OrderDto actual = converter.convert(order);
        assertEquals(expected, actual);
    }
}