package com.epam.esm.service.converter;

import com.epam.esm.repository.model.User;
import com.epam.esm.service.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User source) {
        return new UserDto(source.getId(), source.getName(), source.getPassword());
    }
}
