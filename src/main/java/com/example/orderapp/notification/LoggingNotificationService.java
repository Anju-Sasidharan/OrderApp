package com.example.orderapp.notification;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.orderapp.data.Order;
import org.springframework.stereotype.Service;

@Service
public class LoggingNotificationService {
    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationService.class);

    @Override
    public void sendOrderCreateNotification (Order od){
        log.info("Order Create Notification : \n Order : " + od.getOrderId()
                        + "  Item : " + od.getItem() + " Customer : " + od.getCustomerName()
                        + ", " + od.getCustomerEmail() + "  Quantity : " + od.getQuantity()
                        + "  Price : " + od.getPrice());
    }
}
