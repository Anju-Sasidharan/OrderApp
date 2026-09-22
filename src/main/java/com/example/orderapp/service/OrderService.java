package com.example.orderapp.service;

import com.example.orderapp.dto.OrderRequestDto;
import com.example.orderapp.dto.OrderResponseDto;
import com.example.orderapp.dto.ShipmentUpdateDto;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto request);
    OrderResponseDto getOrder (Long orderId);
    OrderResponseDto processOrder (Long orderId);
    OrderResponseDto shipOrder (Long orderId, ShipmentUpdateDto trackingId);
    OrderResponseDto deliverOrder (Long orderId);
    OrderResponseDto cancelOrder (Long orderId);
}
