package com.brokerage.model;

import com.brokerage.enums.Side;
import com.brokerage.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    @Column(name ="order_id",length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID orderId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "order_side")
    private Side orderSide;

    @Column(name = "size")
    private BigDecimal size;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "status")
    private Status status;

    @Column(name = "create_date")
    private LocalDateTime createDate;
}
