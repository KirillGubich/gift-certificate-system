package com.epam.esm.service.dto;

import com.epam.esm.service.validation.ValidationMessageManager;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class UserDto extends RepresentationModel<UserDto> {

    private int id;

    @NotBlank(message = ValidationMessageManager.BLANK_USER_NAME)
    @Size(min = 3, max = 100, message = ValidationMessageManager.USER_NAME_WRONG_SIZE)
    private String name;

    @Size(min = 8, max = 200, message = ValidationMessageManager.USER_PASSWORD_WRONG_SIZE)
    private String password;

    @NotBlank(message = ValidationMessageManager.BLANK_USER_NAME)
    @Size(min = 3, max = 100, message = ValidationMessageManager.USER_NAME_WRONG_SIZE)
    private String firstName;

    @NotBlank(message = ValidationMessageManager.BLANK_USER_NAME)
    @Size(min = 3, max = 100, message = ValidationMessageManager.USER_NAME_WRONG_SIZE)
    private String lastName;

    public UserDto() {
    }

    public UserDto(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public UserDto(int id, String name, String password, String firstName, String lastName) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id && Objects.equals(name, userDto.name) && Objects.equals(password, userDto.password)
                && Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, password, firstName, lastName);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
