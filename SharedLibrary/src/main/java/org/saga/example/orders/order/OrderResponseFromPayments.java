package org.saga.example.orders.order;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@ToString
public class OrderResponseFromPayments {

    private UUID orderId;

    private OrderState orderState;
}
