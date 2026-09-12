package com.example.orderapp.dto;

import com.example.orderapp.data.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponseDto {
    private Long orderId;
    private String item;
    private Integer quantity;
    private String customerName;
    private String customerEmail;
    private BigDecimal price;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private OrderStatus status;
    private String trackingNo;

}
