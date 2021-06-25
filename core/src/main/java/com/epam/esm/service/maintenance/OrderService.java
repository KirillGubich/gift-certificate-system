package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NoSuchCertificateException;
import com.epam.esm.service.exception.NoSuchOrderException;
import com.epam.esm.service.exception.NoSuchPageException;
import com.epam.esm.service.exception.NoSuchUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements CommonService<OrderDto> {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final GiftCertificateDao certificateDao;
    private final Converter<Order, OrderDto> orderConverter;
    private final Converter<OrderDto, Order> orderDtoConverter;

    @Autowired
    public OrderService(OrderDao orderDao, UserDao userDao, GiftCertificateDao certificateDao,
                        Converter<Order, OrderDto> orderConverter, Converter<OrderDto, Order> orderDtoConverter) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.certificateDao = certificateDao;
        this.orderConverter = orderConverter;
        this.orderDtoConverter = orderDtoConverter;
    }

    @Override
    @Transactional
    public OrderDto create(OrderDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        Order order = orderDtoConverter.convert(dto);
        if (order == null) {
            throw new IllegalArgumentException("null");
        }
        User user = extractUser(dto.getUser());
        List<GiftCertificate> certificates = extractCertificates(dto.getGiftCertificates());
        order.setUser(user);
        order.setGiftCertificates(certificates);
        order.setPurchaseDate(LocalDateTime.now());
        return orderConverter.convert(orderDao.create(order));
    }

    @Override
    public OrderDto read(int id) {
        Optional<Order> orderOptional = orderDao.read(id);
        Order order = orderOptional
                .orElseThrow(() -> new NoSuchOrderException(id));
        return orderConverter.convert(order);
    }

    @Override
    public List<OrderDto> readAll() {
        List<Order> orders = orderDao.readAll();
        return orders.stream()
                .map(orderConverter::convert)
                .collect(Collectors.toList());
    }

    public List<OrderDto> readPaginated(int page, int size) {
        int numberOfPages = orderDao.fetchNumberOfPages(size);
        if (page > numberOfPages) {
            throw new NoSuchPageException(page);
        }
        List<Order> orders = orderDao.readPaginated(page, size);
        return orders.stream()
                .map(orderConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto update(OrderDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        Optional<Order> optionalOrder = orderDao.read(dto.getId());
        Order order = optionalOrder
                .orElseThrow(() -> new NoSuchOrderException(dto.getId()));
        User user = extractUser(dto.getUser());
        List<GiftCertificate> certificates = extractCertificates(dto.getGiftCertificates());
        order.setUser(user);
        order.setGiftCertificates(certificates);
        order.setCost(dto.getCost());
        return orderConverter.convert(orderDao.update(order));
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return orderDao.delete(id);
    }

    private User extractUser(UserDto userDto) {
        Optional<User> user = userDao.read(userDto.getId());
        return user.orElseThrow(() -> new NoSuchUserException(userDto.getId()));
    }

    private List<GiftCertificate> extractCertificates(List<GiftCertificateDto> certificatesDto) {
        List<GiftCertificate> certificates = new ArrayList<>();
        for (GiftCertificateDto dto : certificatesDto) {
            Optional<GiftCertificate> certificateOptional = certificateDao.read(dto.getId());
            certificateOptional
                    .map(certificates::add)
                    .orElseThrow(() -> new NoSuchCertificateException(dto.getId()));
        }
        return certificates;
    }
}
