package org.saga.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="userbalance")
//@Document(collection = "userbalance")
public class UserBalance {

    @Id
    private int userId;

    private int balance;
}
