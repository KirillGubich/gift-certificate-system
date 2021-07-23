package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Role;
import com.epam.esm.service.dto.RoleDto;
import org.springframework.core.convert.converter.Converter;

public class RoleConverter implements Converter<Role, RoleDto> {

    @Override
    public RoleDto convert(Role source) {
        return new RoleDto(source.getId(), source.getName());
    }
}
