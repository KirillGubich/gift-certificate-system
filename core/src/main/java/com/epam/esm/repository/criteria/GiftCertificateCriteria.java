package com.epam.esm.repository.criteria;

import com.epam.esm.repository.model.SortType;
import com.epam.esm.repository.model.SortValue;

import java.util.List;
import java.util.Objects;

public class GiftCertificateCriteria {
    private final String name;
    private final String description;
    private final List<String> tagNames;
    private final SortValue sortValue;
    private final SortType sortType;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public SortValue getSortValue() {
        return sortValue;
    }

    public SortType getSortType() {
        return sortType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateCriteria that = (GiftCertificateCriteria) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description)
                && Objects.equals(tagNames, that.tagNames) && sortValue == that.sortValue && sortType == that.sortType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, tagNames, sortValue, sortType);
    }

    @Override
    public String toString() {
        return "GiftCertificateCriteria{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tagNames=" + tagNames +
                ", sortValue=" + sortValue +
                ", sortType=" + sortType +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private List<String> tagNames;
        private SortValue sortValue;
        private SortType sortType;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withTagNames(List<String> tagNames) {
            this.tagNames = tagNames;
            return this;
        }

        public Builder withSortValue(SortValue sortValue) {
            this.sortValue = sortValue;
            return this;
        }

        public Builder withSortType(SortType sortType) {
            this.sortType = sortType;
            return this;
        }

        public GiftCertificateCriteria build() {
            return new GiftCertificateCriteria(name, description, tagNames, sortValue, sortType);
        }
    }

    private GiftCertificateCriteria(String name, String description, List<String> tagNames,
                                   SortValue sortValue, SortType sortType) {
        this.name = name;
        this.description = description;
        this.tagNames = tagNames;
        this.sortValue = sortValue;
        this.sortType = sortType;
    }
}
