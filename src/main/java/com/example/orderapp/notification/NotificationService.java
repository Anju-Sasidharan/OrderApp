package com.example.orderapp.notification;

import com.example.orderapp.data.Order;

public interface NotificationService {
    void sendOrderCreateNotification(Order od);
}
