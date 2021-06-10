package com.epam.esm.service.dto;

import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.validation.ValidationMessageManager;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public class GiftCertificateDto {
    private int id;

    @NotBlank(message = ValidationMessageManager.BLANK_CERTIFICATE_NAME)
    @Size(min = 3, max = 80, message = ValidationMessageManager.CERTIFICATE_NAME_WRONG_SIZE)
    private String name;

    @NotBlank(message = ValidationMessageManager.BLANK_CERTIFICATE_DESCRIPTION)
    @Size(min = 3, max = 250, message = ValidationMessageManager.CERTIFICATE_DESCRIPTION_WRONG_SIZE)
    private String description;

    @Digits(integer = 10, fraction = 2, message = ValidationMessageManager.CERTIFICATE_PRICE_INVALID)
    @Positive(message = ValidationMessageManager.CERTIFICATE_PRICE_INVALID)
    private BigDecimal price;

    @Digits(integer = 10, fraction = 0, message = ValidationMessageManager.CERTIFICATE_DURATION_INVALID)
    @Positive(message = ValidationMessageManager.CERTIFICATE_DURATION_INVALID)
    private Integer duration;

    private String createDate;
    private String lastUpdateDate;
    private Set<Tag> tags;

    private GiftCertificateDto() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateDto that = (GiftCertificateDto) o;
        return id == that.id && duration == that.duration && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdateDate, that.lastUpdateDate) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate, tags);
    }

    @Override
    public String toString() {
        return "GiftCertificateDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate='" + createDate + '\'' +
                ", lastUpdateDate='" + lastUpdateDate + '\'' +
                ", tags=" + tags +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer duration;
        private String createDate;
        private String lastUpdateDate;
        private Set<Tag> tags;

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder withDuration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public Builder withCreateDate(String createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder withLastUpdateDate(String lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public Builder withTags(Set<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public GiftCertificateDto build() {
            return new GiftCertificateDto(id, name, description, price, duration, createDate, lastUpdateDate, tags);
        }
    }

    private GiftCertificateDto(int id, String name, String description, BigDecimal price, Integer duration,
                               String createDate, String lastUpdateDate, Set<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }
}
