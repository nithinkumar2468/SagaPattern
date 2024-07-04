package org.saga.example.service;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.saga.example.entity.Delivery;
import org.saga.example.model.Restaurant;
import org.saga.example.orders.delivery.OrderResponseFromDelivery;
import org.saga.example.orders.order.OrderState;
import org.saga.example.repository.DeliveryRepository;
import org.saga.example.sqs.OrderEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository repo;

    @Autowired
    private OrderEventPublisher publisher;

    public void send(Restaurant request) throws InterruptedException {
        Delivery delivery = new Delivery();
        delivery.setDeliveryId(UUID.randomUUID());
        delivery.setOrderId(request.getOrderId());
        delivery.setCustomerId(request.getUserId());
        delivery.setHotel(request.getHotelName());
        delivery.setItem(request.getItem());
        delivery.setQuantity(request.getQuantity());
        String time = new Timestamp(System.currentTimeMillis()).toString();
        delivery.setDeliveryTime(time);
        delivery.setDeliveryStatus("Successful");

        repo.save(delivery);

        Thread.sleep(1000);
        OrderResponseFromDelivery response=OrderResponseFromDelivery
                .of(request.getOrderId(),OrderState.ORDER_DELIVERED);
        publisher.publish(response);
    }

}
