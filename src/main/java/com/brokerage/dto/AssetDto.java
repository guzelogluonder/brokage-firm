package com.brokerage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {

    private UUID id;
    private UUID customerId;
    private String assetName;
    private int size;
    private int usableSize;


}
