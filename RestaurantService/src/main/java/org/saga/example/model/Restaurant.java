package org.saga.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Table(name="restaurant")
@Entity
public class Restaurant {

    @Id
    private UUID orderId;
    private Integer hotelId;
    private String hotelName;
    private Integer userId;
    private String Item;
    private Integer quantity;
    private Integer price;
    private String restaurantOrderStatus;
}
