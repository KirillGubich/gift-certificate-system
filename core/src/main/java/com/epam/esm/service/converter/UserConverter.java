package com.epam.esm.service.converter;

import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;
import java.util.stream.Collectors;

public class UserConverter implements Converter<User, UserDto> {

    private final RoleConverter roleConverter;

    public UserConverter(RoleConverter roleConverter) {
        this.roleConverter = roleConverter;
    }

    @Override
    public UserDto convert(User source) {
        Set<RoleDto> roles = source.getRoles().stream().map(roleConverter::convert).collect(Collectors.toSet());
        UserDto userDto = new UserDto(source.getId(), source.getLogin(), source.getPassword(),
                source.getFirstName(), source.getLastName());
        userDto.setRoles(roles);
        return userDto;
    }
}
