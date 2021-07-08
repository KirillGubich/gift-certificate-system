package com.epam.esm.web.model;

import com.epam.esm.service.dto.RoleDto;

import java.util.Objects;
import java.util.Set;

public class AuthenticationInfo {
    private final String login;
    private final String token;
    private final Set<RoleDto> roles;

    public AuthenticationInfo(String login, String token, Set<RoleDto> roles) {
        this.login = login;
        this.token = token;
        this.roles = roles;
    }

    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationInfo that = (AuthenticationInfo) o;
        return Objects.equals(login, that.login) && Objects.equals(token, that.token)
                && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, token, roles);
    }

    @Override
    public String toString() {
        return "AuthenticationInfo{" +
                "login='" + login + '\'' +
                ", token='" + token + '\'' +
                ", roles=" + roles +
                '}';
    }
}
