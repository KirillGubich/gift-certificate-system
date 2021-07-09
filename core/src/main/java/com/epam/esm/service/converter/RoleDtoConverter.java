package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Role;
import com.epam.esm.service.dto.RoleDto;
import org.springframework.core.convert.converter.Converter;

public class RoleDtoConverter implements Converter<RoleDto, Role> {

    @Override
    public Role convert(RoleDto source) {
        return new Role(source.getId(), source.getName());
    }
}
