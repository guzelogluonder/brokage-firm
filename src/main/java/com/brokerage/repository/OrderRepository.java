package com.brokerage.repository;

import com.brokerage.dto.OrderDto;
import com.brokerage.enums.Status;
import com.brokerage.model.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM orders o WHERE o.customerId = :customerId AND o.createDate BETWEEN :startDate AND :endDate")
    List<OrderDto> findAllById(UUID customerId, LocalDateTime startDate, LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query("UPDATE orders o SET o.status = :status WHERE o.customerId = :customerId AND o.id = :id AND o.status = com.brokerage.enums.Status.PENDING")
    void deleteById(@Param("status")Status status, @Param("customerId")UUID customerId, @Param("id")UUID id);

}
