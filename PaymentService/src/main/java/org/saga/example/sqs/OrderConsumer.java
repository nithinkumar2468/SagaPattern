package org.saga.example.sqs;

import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.saga.example.order.model.OrderPurchase;
import org.saga.example.orders.restaurant.PaymentResponseFromRestaurant;
import org.saga.example.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private static final Logger log= LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    private PaymentService service;

    @SqsListener("payment-updates")
    public void consumeOrderPurchase(OrderPurchase orderPurchase){
        log.info("Received Order Status = "+orderPurchase.getOrderStatus()+" for OrderId : "+orderPurchase.getOrderId());
        service.send(orderPurchase);
    }

    @SqsListener("restaurant-failure-updates")
    public void consumePaymentResponse(PaymentResponseFromRestaurant response){
        if(response!=null) {
            log.info("Received Order Status : " + response.getOrderState() + " from Restaurant for orderId : " + response.getOrderId());
            service.update(response);
        }
    }
}
