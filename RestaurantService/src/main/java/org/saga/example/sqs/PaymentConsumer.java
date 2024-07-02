package org.saga.example.sqs;

import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.saga.example.entity.Payment;
import org.saga.example.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    private static final Logger log= LoggerFactory.getLogger(PaymentConsumer.class);

    @Autowired
    private RestaurantService service;

    @SqsListener("restaurant-updates")
    public void consume(Payment payment){
        log.info("Received Order details for OrderId : "+payment.getOrderId());
        service.send(payment);
    }

}
