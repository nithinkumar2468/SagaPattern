package org.saga.example.orders.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PaymentDto {

    private UUID orderId;
    private Integer userId;
    private Integer amount;

}
