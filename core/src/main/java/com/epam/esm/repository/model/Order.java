package com.epam.esm.repository.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = DatabaseInfo.ORDER_TABLE)
@NamedQueries({
        @NamedQuery(name = "Order_findAll", query = "SELECT o FROM Order as o"),
        @NamedQuery(name = "Order_getAmount", query = "SELECT count(o.id) FROM Order as o")
})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = DatabaseInfo.ORDER_ID_COLUMN)
    private int id;
    private BigDecimal cost;
    @Column(name = DatabaseInfo.PURCHASE_DATE_COLUMN)
    private LocalDateTime purchaseDate;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = DatabaseInfo.USER_ID_COLUMN)
    private User user;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinTable(name = DatabaseInfo.CERTIFICATE_ORDER_TABLE,
            joinColumns = @JoinColumn(name = DatabaseInfo.ORDER_ID_COLUMN),
            inverseJoinColumns = @JoinColumn(name = DatabaseInfo.CERTIFICATE_ID_COLUMN))
    private List<GiftCertificate> giftCertificates;

    public Order() {
    }

    public Order(int id, BigDecimal cost, LocalDateTime purchaseDate, User user,
                 List<GiftCertificate> giftCertificates) {
        this.id = id;
        this.cost = cost;
        this.purchaseDate = purchaseDate;
        this.user = user;
        this.giftCertificates = giftCertificates;
    }

    @PrePersist
    public void onPrePersist() {
        purchaseDate = LocalDateTime.now();
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

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<GiftCertificate> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(List<GiftCertificate> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(cost, order.cost) && Objects.equals(purchaseDate, order.purchaseDate)
                && Objects.equals(user, order.user) && Objects.equals(giftCertificates, order.giftCertificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, purchaseDate, user, giftCertificates);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", cost=" + cost +
                ", purchaseDate=" + purchaseDate +
                ", giftCertificates=" + giftCertificates +
                '}';
    }
}
