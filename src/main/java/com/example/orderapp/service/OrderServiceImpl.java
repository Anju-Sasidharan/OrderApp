package com.example.orderapp.service;

import com.example.orderapp.data.Order;
import com.example.orderapp.data.OrderStatus;
import com.example.orderapp.dto.OrderRequestDto;
import com.example.orderapp.dto.OrderResponseDto;
import com.example.orderapp.dto.ShipmentUpdateDto;
import com.example.orderapp.exception.OrderNotFoundException;
import com.example.orderapp.notification.NotificationService;
import com.example.orderapp.repository.OrderRepository;
import org.springframework.stereotype.Service;
import com.example.orderapp.notification.NotificationService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    public OrderServiceImpl(OrderRepository orderRepository, NotificationService notificationService){
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }
    @Override
    public OrderResponseDto createOrder(OrderRequestDto request) {
        Order od = new Order();
        od.setItem(request.getItem());
        od.setQuantity(request.getQuantity());
        od.setCustomerName(request.getCustomerName());
        od.setCustomerEmail(request.getCustomerEmail());
        od.setAddress(request.getAddress());
        od.setPrice(request.getPrice());
        od.setCreatedAt(LocalDateTime.now());
        od.setUpdatedAt(LocalDateTime.now());
        od.setStatus(OrderStatus.CREATED);

        Order saved = orderRepository.save(od);
        notificationService.sendOrderCreateNotification(saved);
        return toResponseDto(saved);
    }

    @Override
    public OrderResponseDto processOrder(Long orderId){
      Order od = findOrder(orderId);
      validateTransition(od.getStatus(), OrderStatus.PROCESSING);
      od.setStatus(OrderStatus.PROCESSING);
      od.setUpdatedAt(LocalDateTime.now());
      return toResponseDto( orderRepository.save(od));
    }

    @Override
    public OrderResponseDto shipOrder(Long orderId, ShipmentUpdateDto shipment){
        Order od = findOrder(orderId);
        validateTransition(od.getStatus(),OrderStatus.SHIPPED);
        od.setTrackingNo(shipment.getTrackingNo());
        od.setStatus(OrderStatus.SHIPPED);
        od.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(od);
        return toResponseDto(saved);
    }
    @Override
    public OrderResponseDto deliverOrder (Long orderId) {
        Order od = findOrder(orderId);
        validateTransition(od.getStatus(),OrderStatus.DELIVERED);
        od.setStatus(OrderStatus.DELIVERED);
        od.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(od);
        return toResponseDto(saved);
    }

    @Override
    public OrderResponseDto cancelOrder(Long orderId){
        Order od = findOrder(orderId);
        validateTransition(od.getStatus(),OrderStatus.CANCELLED);
        od.setUpdatedAt(LocalDateTime.now());
        od.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(od);
        return toResponseDto(saved);
    }

    @Override
    public OrderResponseDto getOrder (Long orderId){
        Order od = findOrder(orderId);
        return toResponseDto(od);
    }

    private Order findOrder(Long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order not found with id : "+orderId));;
    }

    private void validateTransition(OrderStatus current , OrderStatus next){
        boolean valid = switch (current){
            case CREATED -> next ==  OrderStatus.PROCESSING || next == OrderStatus.CANCELLED;
            case PROCESSING -> next ==OrderStatus.SHIPPED || next ==OrderStatus.CANCELLED;
            case SHIPPED ->  next == OrderStatus.DELIVERED;
            case DELIVERED,CANCELLED -> false;
        };
    }
    private OrderResponseDto toResponseDto(Order od){
        OrderResponseDto response = new OrderResponseDto();
        response.setOrderId(od.getOrderId());
        response.setItem(od.getItem());
        response.setQuantity(od.getQuantity());
        response.setCustomerName(od.getCustomerName());
        response.setCustomerEmail(od.getCustomerEmail());
        response.setAddress(od.getAddress());
        response.setPrice(od.getPrice());
        response.setStatus(od.getStatus());
        response.setTrackingNo(od.getTrackingNo());
        response.setCreatedAt(od.getCreatedAt());
        response.setUpdatedAt(od.getUpdatedAt());
        return response;
    }
}
