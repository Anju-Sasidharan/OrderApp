package com.example.orderapp.controller;

import com.example.orderapp.dto.OrderRequestDto;
import com.example.orderapp.dto.OrderResponseDto;
import com.example.orderapp.dto.ShipmentUpdateDto;
import com.example.orderapp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto request){
        OrderResponseDto response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId){
        OrderResponseDto response = orderService.getOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/process")
    public ResponseEntity<OrderResponseDto> processOrder(@PathVariable Long orderId) {
        OrderResponseDto response = orderService.processOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/ship")
    public ResponseEntity<OrderResponseDto> shipOrder(@PathVariable Long orderId, @Valid @RequestBody ShipmentUpdateDto shipment) {
        OrderResponseDto shipped = orderService.shipOrder(orderId, shipment);
        return ResponseEntity.ok(shipped);
    }

    @PatchMapping("/{orderId}/deliver")
    public ResponseEntity<OrderResponseDto> deliverOrder(@PathVariable Long orderId) {
        OrderResponseDto response = orderService.deliverOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId) {
        OrderResponseDto response = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(response);
    }
}
