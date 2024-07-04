package org.saga.example.orders.delivery;

import lombok.*;
import org.saga.example.orders.order.OrderState;

import java.util.UUID;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@ToString
public class OrderResponseFromDelivery {
    private UUID orderId;

    private OrderState orderState;
}
