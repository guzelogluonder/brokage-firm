package com.brokerage.dto;

import com.brokerage.enums.Side;
import com.brokerage.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private UUID id;
    private UUID customerId;
    private String assetName;
    private Side orderSide;
    private BigDecimal size;
    private BigDecimal price;
    private Status status;
    private LocalDateTime createDate;

}
