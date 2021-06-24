package com.epam.esm.repository.criteria;

import java.util.List;
import java.util.Objects;

public class GiftCertificateCriteria {

    private String name;
    private String description;
    private List<String> tagNames;

    public GiftCertificateCriteria(String name, String description, List<String> tagNames) {
        this.name = name;
        this.description = description;
        this.tagNames = tagNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateCriteria that = (GiftCertificateCriteria) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description)
                && Objects.equals(tagNames, that.tagNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, tagNames);
    }

    @Override
    public String toString() {
        return "GiftCertificateCriteria{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tagNames=" + tagNames +
                '}';
    }
}
