package com.epam.esm.service.dto;

import com.epam.esm.service.validation.ValidationMessageManager;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class UserDto {

    private int id;

    @NotBlank(message = ValidationMessageManager.BLANK_USER_NAME)
    @Size(min = 3, max = 100, message = ValidationMessageManager.USER_NAME_WRONG_SIZE)
    private String name;

    @Size(min = 8, max = 200, message = ValidationMessageManager.USER_PASSWORD_WRONG_SIZE)
    private String password;

    public UserDto() {
    }

    public UserDto(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id && Objects.equals(name, userDto.name) && Objects.equals(password, userDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
