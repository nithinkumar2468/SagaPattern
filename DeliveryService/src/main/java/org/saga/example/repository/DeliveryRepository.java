package org.saga.example.repository;

import org.saga.example.entity.Delivery;
import org.saga.example.service.DeliveryService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
}
