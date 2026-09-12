package com.example.orderapp.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table (name="orders" )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    private String item;
    private Integer quantity;
    private String customerName;
    private String customerEmail;
    private BigDecimal price;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String trackingNo;
}
