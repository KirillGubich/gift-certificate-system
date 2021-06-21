package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.converter.UserConverter;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NoSuchUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements CommonService<UserDto> {

    private final UserDao userDao;
    private final UserConverter userConverter;

    @Autowired
    public UserService(UserDao userDao, UserConverter userConverter) {
        this.userDao = userDao;
        this.userConverter = userConverter;
    }

    @Override
    public UserDto create(UserDto dto) {
        throw new UnsupportedOperationException("User: create");
    }

    @Override
    public UserDto read(int id) {
        Optional<User> user = userDao.read(id);
        if (!user.isPresent()) {
            throw new NoSuchUserException(id);
        }
        return userConverter.convert(user.get());
    }

    @Override
    public List<UserDto> readAll() {
        List<User> users = userDao.readAll();
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
}
