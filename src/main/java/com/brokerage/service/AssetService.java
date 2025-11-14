package com.brokerage.service;

import com.brokerage.model.Asset;
import com.brokerage.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public List<Asset> getAssetById(@PathVariable UUID id) {
        return assetRepository.findAllById(Collections.singleton(id));
    }

    public List<Asset> getAllAsset(){
        return assetRepository.findAll();
    }

}
