package org.saga.example.sqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.saga.example.model.Restaurant;
import org.saga.example.service.DeliveryService;
import org.saga.example.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantConsumer {

    private static final Logger log= LoggerFactory.getLogger(RestaurantConsumer.class);

    @Autowired
    private DeliveryService service;

    @SqsListener("delivery-updates")
    public void consume(Restaurant request){
        log.info("Received Order Status for orderId :" +request.getOrderId());
        service.send(request);
    }
}
