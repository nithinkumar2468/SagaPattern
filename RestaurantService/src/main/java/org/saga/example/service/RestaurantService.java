package org.saga.example.service;

import org.saga.example.entity.Payment;
import org.saga.example.model.Hotel;
import org.saga.example.model.Products;
import org.saga.example.model.Restaurant;
import org.saga.example.orders.order.OrderState;
import org.saga.example.orders.restaurant.OrderResponseFromRestaurant;
import org.saga.example.orders.restaurant.PaymentResponseFromRestaurant;
import org.saga.example.repository.HotelRepository;
import org.saga.example.repository.ProductsRepository;
import org.saga.example.repository.RestaurantRepository;
import org.saga.example.sqs.RestaurantPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repo;
    @Autowired
    private HotelRepository hrepo;
    @Autowired
    private ProductsRepository prodRepo;
    @Autowired
    private RestaurantPublisher publisher;

    public void send(Payment payment) {

        Hotel hotel = hrepo.findById(payment.getHotelId()).get();

        Products product = prodRepo.findById(payment.getProductId()).get();

        Restaurant restaurant = new Restaurant();
        restaurant.setOrderId(payment.getOrderId());
        restaurant.setHotelId(hotel.getHotelId());
        restaurant.setHotelName(hotel.getHotelName());
        restaurant.setQuantity(payment.getQuantity());
        restaurant.setPrice(payment.getAmount());
        restaurant.setItem(product.getItem());
        restaurant.setUserId(payment.getCustomerId());

        if (product.getQuantity() >= payment.getQuantity()) {

            restaurant.setRestaurantOrderStatus("success");
            repo.save(restaurant);

            publisher.publish(restaurant);

            product.setQuantity(product.getQuantity() - payment.getQuantity());
            prodRepo.save(product);

            OrderResponseFromRestaurant response = OrderResponseFromRestaurant.of(payment.getOrderId(), OrderState.ORDER_PREPARED);
            publisher.publish(response);
        } else {
            restaurant.setRestaurantOrderStatus("failure");
            repo.save(restaurant);

            PaymentResponseFromRestaurant res = PaymentResponseFromRestaurant.of(payment.getOrderId(), payment.getPaymentId(), payment.getAmount(), OrderState.ORDER_FAILED);
            publisher.publish(res);

            OrderResponseFromRestaurant response = OrderResponseFromRestaurant.of(payment.getOrderId(), OrderState.ORDER_FAILED);
            publisher.publish(response);
        }
    }

    public List<Restaurant> getAllRestaurant() {
        return repo.findAll();
    }
}
