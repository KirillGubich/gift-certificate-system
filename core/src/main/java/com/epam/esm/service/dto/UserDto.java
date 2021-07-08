package com.epam.esm.service.dto;

import com.epam.esm.service.validation.ValidationMessageManager;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

public class UserDto extends RepresentationModel<UserDto> {

    private int id;

    @NotBlank(message = ValidationMessageManager.BLANK_USER_NAME)
    @Size(min = 3, max = 100, message = ValidationMessageManager.USER_NAME_WRONG_SIZE)
    private String login;

    @Size(min = 8, max = 200, message = ValidationMessageManager.USER_PASSWORD_WRONG_SIZE)
    private String password;

    @NotBlank(message = ValidationMessageManager.BLANK_USER_NAME)
    @Size(min = 3, max = 100, message = ValidationMessageManager.USER_NAME_WRONG_SIZE)
    private String firstName;

    @NotBlank(message = ValidationMessageManager.BLANK_USER_NAME)
    @Size(min = 3, max = 100, message = ValidationMessageManager.USER_NAME_WRONG_SIZE)
    private String lastName;

    private Set<RoleDto> roles;

    public UserDto() {
    }

    public UserDto(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public UserDto(int id, String login, String password, String firstName, String lastName, Set<RoleDto> roles) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id && Objects.equals(login, userDto.login) && Objects.equals(password, userDto.password)
                && Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName)
                && Objects.equals(roles, userDto.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, login, password, firstName, lastName, roles);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roles=" + roles +
                '}';
    }
}
