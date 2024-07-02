package org.saga.example.sqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.saga.example.entity.Payment;
import org.saga.example.orders.order.OrderResponseFromPayments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentPublisher {

    private static final Logger log= LoggerFactory.getLogger(PaymentPublisher.class);

    @Autowired
    private QueueMessagingTemplate msgTemplate;

    public PaymentPublisher(AmazonSQSAsync amazonSQSAsync) {
        this.msgTemplate=new QueueMessagingTemplate(amazonSQSAsync);
    }


    public void publish(Payment payment) {
        log.info("Publishing Event for OrderId :" +payment.getOrderId());
        msgTemplate.convertAndSend("restaurant-updates",payment);
    }

    public void publish(OrderResponseFromPayments response){
        log.info("Publishing OrderStatus to OrderId : "+response.getOrderId());
        msgTemplate.convertAndSend("order-updates",response);
    }
}
