package com.example.orderapp.repository;

import com.example.orderapp.data.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
