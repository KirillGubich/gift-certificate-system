package com.epam.esm.service.dto;

import com.epam.esm.service.validation.ValidationMessageManager;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class OrderDto extends RepresentationModel<OrderDto> {

    private int id;

    @Digits(integer = 10, fraction = 2, message = ValidationMessageManager.ORDER_COST_INVALID)
    @Positive(message = ValidationMessageManager.ORDER_COST_INVALID)
    private BigDecimal cost;
    private String purchaseDate;
    private UserDto user;
    private List<GiftCertificateDto> giftCertificates;

    public OrderDto() {
    }

    public OrderDto(int id, BigDecimal cost, String purchaseDate, UserDto user,
                    List<GiftCertificateDto> giftCertificates) {
        this.id = id;
        this.cost = cost;
        this.purchaseDate = purchaseDate;
        this.user = user;
        this.giftCertificates = giftCertificates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<GiftCertificateDto> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(List<GiftCertificateDto> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return id == orderDto.id && Objects.equals(cost, orderDto.cost)
                && Objects.equals(purchaseDate, orderDto.purchaseDate) && Objects.equals(user, orderDto.user)
                && Objects.equals(giftCertificates, orderDto.giftCertificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, purchaseDate, user, giftCertificates);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", cost=" + cost +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", user=" + user +
                ", giftCertificates=" + giftCertificates +
                '}';
    }
}
