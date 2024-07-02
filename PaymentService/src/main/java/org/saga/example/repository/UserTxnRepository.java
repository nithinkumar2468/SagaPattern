package org.saga.example.repository;

import org.saga.example.entity.UserTxn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTxnRepository extends JpaRepository<UserTxn, UUID> {
}
