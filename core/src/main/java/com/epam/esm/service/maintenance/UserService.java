package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.UserRepository;
import com.epam.esm.repository.model.Role;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NoSuchPageException;
import com.epam.esm.service.exception.NoSuchUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements CommonService<UserDto>, UserDetailsService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final Set<Role> userRoleSet = new HashSet<>();

    @Autowired
    public UserService(UserRepository userRepository, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
        Role userRole = new Role(2, "USER");
        userRoleSet.add(userRole);
    }

    @Override
    @Transactional
    public UserDto create(UserDto dto) {
        return update(dto);
    }

    @Override
    public UserDto read(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional
                .orElseThrow(() -> new NoSuchUserException(id));
        return conversionService.convert(user, UserDto.class);
    }

    @Override
    public List<UserDto> readAll() {
        List<User> users = new ArrayList<>();
        Iterable<User> userIterable = userRepository.findAll();
        userIterable.forEach(users::add);
        return users.stream()
                .map(user -> conversionService.convert(user, UserDto.class))
                .collect(Collectors.toList());
    }

    public List<UserDto> readPaginated(int page, int size) {
        int numberOfPages = getLastPage(size);
        if (page > numberOfPages) {
            throw new NoSuchPageException(page);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<User> allUsers = userRepository.findAll(pageRequest);
        List<User> users = allUsers.get().collect(Collectors.toList());
        return users.stream()
                .map(user -> conversionService.convert(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto update(UserDto dto) {
        User user = conversionService.convert(dto, User.class);
        if (user == null) {
            throw new IllegalArgumentException();
        }
        user.setRoles(userRoleSet);
        User updatedUser = userRepository.saveAndFlush(user);
        return conversionService.convert(updatedUser, UserDto.class);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public List<OrderDto> readUserOrders(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional
                .orElseThrow(() -> new NoSuchUserException(id));
        return user.getOrders().stream()
                .map(order -> conversionService.convert(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    public int getLastPage(int size) {
        int count = (int) userRepository.count();
        int pages = count / size;
        if (count % size > 0) {
            pages++;
        }
        return pages;
    }

    @Transactional
    public UserDto getByLogin(String login) {
        User user = userRepository.findByLogin(login);
        if (user != null) {
            return conversionService.convert(user, UserDto.class);
        } else {
            throw new NoSuchUserException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByLogin(username);
        if (Objects.isNull(u)) {
            throw new UsernameNotFoundException(String.format("User %s is not found", username));
        }
        String[] roles = u.getRoles().stream().map(Role::getName).toArray(String[]::new);
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getLogin())
                .password(u.getPassword())
                .roles(roles)
                .build();
    }
}
