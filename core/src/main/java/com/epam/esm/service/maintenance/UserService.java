package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NoSuchPageException;
import com.epam.esm.service.exception.NoSuchUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements CommonService<UserDto> {
    private final UserDao userDao;
    private final Converter<User, UserDto> userConverter;
    private final Converter<Order, OrderDto> orderConverter;

    @Autowired
    public UserService(UserDao userDao, Converter<User, UserDto> userConverter, Converter<Order,
            OrderDto> orderConverter) {
        this.userDao = userDao;
        this.userConverter = userConverter;
        this.orderConverter = orderConverter;
    }

    @Override
    public UserDto create(UserDto dto) {
        throw new UnsupportedOperationException("User: create");
    }

    @Override
    public UserDto read(int id) {
        Optional<User> userOptional = userDao.read(id);
        User user = userOptional
                .orElseThrow(() -> new NoSuchUserException(id));
        return userConverter.convert(user);
    }

    @Override
    public List<UserDto> readAll() {
        List<User> users = userDao.readAll();
        return users.stream()
                .map(userConverter::convert)
                .collect(Collectors.toList());
    }

    public List<UserDto> readPaginated(int page, int size) {
        int numberOfPages = userDao.fetchNumberOfPages(size);
        if (page > numberOfPages) {
            throw new NoSuchPageException(page);
        }
        List<User> users = userDao.readPaginated(page, size);
        return users.stream()
                .map(userConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto dto) {
        throw new UnsupportedOperationException("User: update");
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("User: delete");
    }

    @Transactional
    public List<OrderDto> readUserOrders(int id) {
        Optional<User> userOptional = userDao.read(id);
        User user = userOptional
                .orElseThrow(() -> new NoSuchUserException(id));
        return user.getOrders().stream()
                .map(orderConverter::convert)
                .collect(Collectors.toList());
    }
}
