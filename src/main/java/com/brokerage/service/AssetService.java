package com.brokerage.service;

import com.brokerage.dto.AssetDto;
import com.brokerage.dto.OrderDto;
import com.brokerage.enums.Side;
import com.brokerage.exception.InsufficientBalanceException;
import com.brokerage.model.Asset;
import com.brokerage.model.Order;
import com.brokerage.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import com.brokerage.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public List<Asset> getAssetById(@PathVariable UUID id) {
        return assetRepository.findAllById(Collections.singleton(id));
    }

    public List<Asset> getAllAsset() {
        return assetRepository.findAll();
    }


}
