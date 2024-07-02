package org.saga.example.sqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.saga.example.model.Restaurant;
import org.saga.example.orders.restaurant.OrderResponseFromRestaurant;
import org.saga.example.orders.restaurant.PaymentResponseFromRestaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantPublisher {

    private static final Logger log= LoggerFactory.getLogger(RestaurantPublisher.class);

    @Autowired
    private QueueMessagingTemplate msgTemplate;

    public RestaurantPublisher(AmazonSQSAsync amazonSQSAsync) {
        this.msgTemplate=new QueueMessagingTemplate(amazonSQSAsync);
    }

    public void publish(OrderResponseFromRestaurant response) {
        log.info("Publishing OrderId :" +response.getOrderId());
        msgTemplate.convertAndSend("order-updates",response);
    }

    public void publish(Restaurant resModel){
        msgTemplate.convertAndSend("delivery-updates",resModel);
    }

    public void publish(PaymentResponseFromRestaurant res){
        msgTemplate.convertAndSend("restaurant-failure-updates",res);
    }
}
