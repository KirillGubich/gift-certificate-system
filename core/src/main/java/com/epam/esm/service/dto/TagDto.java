package com.epam.esm.service.dto;

import com.epam.esm.service.validation.ValidationMessageManager;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class TagDto extends RepresentationModel<TagDto> {
    private int id;

    @NotBlank(message = ValidationMessageManager.BLANK_TAG_NAME)
    @Size(min = 3, max = 50, message = ValidationMessageManager.TAG_NAME_WRONG_SIZE)
    private String name;

    public TagDto() {
    }

    public TagDto(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDto tagDto = (TagDto) o;
        return id == tagDto.id && Objects.equals(name, tagDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "TagDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
