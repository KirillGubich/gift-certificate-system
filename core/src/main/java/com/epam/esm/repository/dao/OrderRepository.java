package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
