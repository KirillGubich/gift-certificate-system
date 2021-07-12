package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query(value = "SELECT id, name FROM tags LEFT JOIN certificate_tag ct " +
            "on tags.id = ct.tag_id WHERE certificate_id IN (SELECT co.certificate_id FROM gift_certificates JOIN " +
            "certificate_order co on gift_certificates.id = co.certificate_id WHERE order_id IN (SELECT " +
            "orders.order_id FROM orders WHERE user_id = (SELECT users.user_id FROM users RIGHT JOIN orders o ON " +
            "users.user_id = o.user_id GROUP BY users.user_id ORDER BY SUM(cost) desc LIMIT 1))) GROUP BY id " +
            "ORDER BY COUNT(id) desc LIMIT 1", nativeQuery = true)
    Tag findMostWidelyUsedTag();

    Optional<Tag> findByName(String name);
}
