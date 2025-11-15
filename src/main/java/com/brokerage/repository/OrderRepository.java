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

    @Query("SELECT o FROM orders o WHERE o.customerId = :customerId " +
            "AND o.createDate >= :startDate AND o.createDate <= :endDate")
    List<Order> findOrdersByCustomerAndDateRange(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<Order> findByCustomerId(Long customerId);


}
