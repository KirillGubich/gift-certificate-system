package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.GiftCertificateRepository;
import com.epam.esm.repository.dao.OrderRepository;
import com.epam.esm.repository.dao.UserRepository;
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
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements CommonService<OrderDto> {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository certificateRepository;
    private final ConversionService conversionService;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        GiftCertificateRepository certificateRepository, ConversionService conversionService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
        this.conversionService = conversionService;
    }

    @Override
    @Transactional
    public OrderDto create(OrderDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        Order order = conversionService.convert(dto, Order.class);
        if (order == null) {
            throw new IllegalArgumentException("null");
        }
        User user = extractUser();
        List<GiftCertificate> certificates = extractCertificates(dto.getGiftCertificates());
        order.setUser(user);
        order.setGiftCertificates(certificates);
        return conversionService.convert(orderRepository.saveAndFlush(order), OrderDto.class);
    }

    @Override
    public OrderDto read(int id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        Order order = orderOptional
                .orElseThrow(() -> new NoSuchOrderException(id));
        return conversionService.convert(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> readAll() {
        List<Order> orders = new ArrayList<>();
        final Iterable<Order> allOrders = orderRepository.findAll();
        allOrders.forEach(orders::add);
        return orders.stream()
                .map(order -> conversionService.convert(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    public List<OrderDto> readPaginated(int page, int size) {
        int numberOfPages = getLastPage(size);
        if (page > numberOfPages) {
            throw new NoSuchPageException(page);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        final Page<Order> allOrders = orderRepository.findAll(pageRequest);
        final List<Order> orders = allOrders.get().collect(Collectors.toList());
        return orders.stream()
                .map(order -> conversionService.convert(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto update(OrderDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        Optional<Order> optionalOrder = orderRepository.findById(dto.getId());
        Order order = optionalOrder
                .orElseThrow(() -> new NoSuchOrderException(dto.getId()));
        User user = extractUser(dto.getUser());
        List<GiftCertificate> certificates = extractCertificates(dto.getGiftCertificates());
        order.setUser(user);
        order.setGiftCertificates(certificates);
        order.setCost(dto.getCost());
        return conversionService.convert(orderRepository.saveAndFlush(order), OrderDto.class);
    }

    @Override
    @Transactional
    public void delete(int id) {
        orderRepository.deleteById(id);
    }

    public int getLastPage(int size) {
        int count = (int) orderRepository.count();
        int pages = count / size;
        if (count % size > 0) {
            pages++;
        }
        return pages;
    }

    private User extractUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login;
        if (principal instanceof UserDetails) {
            login = ((UserDetails) principal).getUsername();
        } else {
            login = principal.toString();
        }
        User user = userRepository.findByLogin(login);
        if (user != null) {
            return user;
        } else {
            throw new NoSuchUserException();
        }
    }

    private User extractUser(UserDto userDto) {
        Optional<User> user = userRepository.findById(userDto.getId());
        return user.orElseThrow(() -> new NoSuchUserException(userDto.getId()));
    }

    private List<GiftCertificate> extractCertificates(List<GiftCertificateDto> certificatesDto) {
        List<GiftCertificate> certificates = new ArrayList<>();
        for (GiftCertificateDto dto : certificatesDto) {
            Optional<GiftCertificate> certificateOptional = certificateRepository.findById(dto.getId());
            certificateOptional
                    .map(certificates::add)
                    .orElseThrow(() -> new NoSuchCertificateException(dto.getId()));
        }
        return certificates;
    }
}
