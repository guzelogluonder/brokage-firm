package com.brokerage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {

    private UUID id;
    private UUID customerId;
    private String assetName;
    private BigDecimal size;
    private BigDecimal usableSize;


}
