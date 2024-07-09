package org.saga.example.orders.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saga.example.orders.order.OrderStatus;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class OrderPurchaseDTO {

    private UUID orderId;
    private Integer customerId;
    private Integer price;
    private Integer productId;
    private Integer hotelId;
    private Integer quantity;
    private String paymentMethod;
}
