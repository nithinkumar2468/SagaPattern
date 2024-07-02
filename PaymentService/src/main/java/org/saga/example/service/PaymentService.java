package org.saga.example.service;

import org.saga.example.entity.Payment;
import org.saga.example.entity.UserBalance;
import org.saga.example.entity.UserTxn;
import org.saga.example.order.model.OrderPurchase;
import org.saga.example.orders.order.OrderResponseFromPayments;
import org.saga.example.orders.order.OrderState;
import org.saga.example.orders.payment.PaymentStatus;
import org.saga.example.orders.restaurant.PaymentResponseFromRestaurant;
import org.saga.example.repository.PaymentRepository;
import org.saga.example.repository.UserBalanceRepository;
import org.saga.example.repository.UserTxnRepository;
import org.saga.example.sqs.PaymentPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository repo;

    @Autowired
    private UserBalanceRepository ubrepo;

    @Autowired
    private UserTxnRepository utrepo;
    @Autowired
    private PaymentPublisher paymentPublisher;


    @Transactional
    public void send(OrderPurchase orderPurchase) {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(orderPurchase.getOrderId());
        payment.setCustomerId(orderPurchase.getCustomerId());
        payment.setProductId(orderPurchase.getProductId());
        payment.setHotelId(orderPurchase.getHotelId());
        payment.setQuantity(orderPurchase.getQuantity());
        payment.setPaymentMethod(orderPurchase.getPaymentMethod());
        payment.setAmount(orderPurchase.getPrice());

        UserBalance ub = ubrepo.findById(orderPurchase.getCustomerId()).get();
        log.info(String.valueOf(ub));

        if (ub.getBalance() >= orderPurchase.getPrice()) {
            ubrepo.findById(orderPurchase.getCustomerId())
                    .filter(u -> u.getBalance() >= orderPurchase.getPrice())
                    .map(uba -> {
                        payment.setPaymentStatus(String.valueOf(PaymentStatus.SUCCESS));

                        repo.save(payment);
                        paymentPublisher.publish(payment);

                        uba.setBalance(uba.getBalance() - orderPurchase.getPrice());
                        ubrepo.save(uba);

                        OrderResponseFromPayments response = OrderResponseFromPayments.of(orderPurchase.getOrderId(), OrderState.ORDER_PAID);
                        paymentPublisher.publish(response);

                        String time = new Timestamp(System.currentTimeMillis()).toString();
                        utrepo.save(UserTxn.of(orderPurchase.getOrderId(), uba.getUserId(), orderPurchase.getPrice(), time));
                        return true;
                    });
        } else {
            payment.setPaymentStatus(String.valueOf(PaymentStatus.FAILURE));
            repo.save(payment);
            OrderResponseFromPayments response = OrderResponseFromPayments.of(orderPurchase.getOrderId(), OrderState.ORDER_FAILED);
            paymentPublisher.publish(response);
        }
    }

    public void update(PaymentResponseFromRestaurant response) {
        Payment payment = repo.findById(response.getPaymentId()).get();
        payment.setPaymentStatus(String.valueOf(PaymentStatus.REFUNDED));
        repo.save(payment);

        UserBalance balance = ubrepo.findById(payment.getCustomerId()).get();
        balance.setBalance(balance.getBalance() + response.getAmount());
        ubrepo.save(balance);
    }
}