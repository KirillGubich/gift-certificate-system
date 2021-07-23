package com.epam.esm.repository.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = DatabaseInfo.TAG_TABLE)
@NamedQueries({
        @NamedQuery(name = "Tag_findAll", query = "SELECT t FROM Tag as t"),
        @NamedQuery(name = "Tag_getAmount", query = "SELECT count(t.id) FROM Tag as t"),
        @NamedQuery(name = "Tag_findByName", query = "SELECT t FROM Tag as t WHERE t.name=:name")
})
@NamedNativeQuery(name = "Tag_getMostUsedTag", query = "SELECT id, name FROM tags LEFT JOIN certificate_tag ct " +
        "on tags.id = ct.tag_id WHERE certificate_id IN (SELECT co.certificate_id FROM gift_certificates JOIN " +
        "certificate_order co on gift_certificates.id = co.certificate_id WHERE order_id IN (SELECT orders.order_id " +
        "FROM orders WHERE user_id = (SELECT users.user_id FROM users RIGHT JOIN orders o ON " +
        "users.user_id = o.user_id GROUP BY users.user_id ORDER BY SUM(cost) desc LIMIT 1))) GROUP BY id " +
        "ORDER BY COUNT(id) desc LIMIT 1", resultClass = Tag.class)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<GiftCertificate> certificates;

    public Tag() {
    }

    public Tag(int id, String name) {
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

    public List<GiftCertificate> getCertificates() {
        return certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
