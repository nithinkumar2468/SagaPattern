package org.saga.example.orders.restaurant;

import lombok.*;
import org.saga.example.orders.order.OrderState;

import java.util.UUID;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@ToString
public class OrderResponseFromRestaurant {

    private UUID orderId;
    private OrderState orderState;
}
