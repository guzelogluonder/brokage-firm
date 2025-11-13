package com.brokerage.model;

import com.brokerage.enums.Side;
import com.brokerage.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "order_side")
    private Side orderSide;

    @Column(name = "size")
    private String size;

    @Column(name = "price")
    private double price;

    @Column(name = "status")
    private Status status;

    @Column(name = "createDate")
    private LocalDateTime createDate;
}
