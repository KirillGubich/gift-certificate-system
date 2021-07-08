package com.epam.esm.web.security;

import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.maintenance.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AccessValidator {

    private final UserService userService;

    @Autowired
    public AccessValidator(UserService userService) {
        this.userService = userService;
    }

    public void validate(int id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login;
        if (principal instanceof UserDetails) {
            login = ((UserDetails) principal).getUsername();
        } else {
            login = principal.toString();
        }
        UserDto user = userService.getByLogin(login);
        Set<String> roles = user.getRoles().stream().map(RoleDto::getName).collect(Collectors.toSet());
        boolean accessAllowed = roles.contains("ADMIN") || roles.contains("USER") && id == user.getId();
        if (!accessAllowed) {
            throw new AccessDeniedException(Integer.toString(id));
        }
    }
}
