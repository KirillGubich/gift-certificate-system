package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Role;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.UserDto;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDtoConverter implements Converter<UserDto, User> {

    private final RoleDtoConverter roleConverter;

    public UserDtoConverter(RoleDtoConverter roleConverter) {
        this.roleConverter = roleConverter;
    }

    @Override
    public User convert(UserDto source) {
        Set<Role> roles = source.getRoles().stream().map(roleConverter::convert).collect(Collectors.toSet());
        return new User(source.getId(), source.getLogin(), source.getPassword(),
                source.getFirstName(), source.getLastName(), roles);
    }
}
