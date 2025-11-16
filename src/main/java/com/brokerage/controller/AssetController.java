package com.brokerage.controller;

import com.brokerage.model.Asset;
import com.brokerage.service.AssetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class AssetController {

    private final AssetService assetService;

    public static final String ASSET_PATH = "/api/assets";
    public static final String ASSET_PATH_ID = "/api/assets/{customerId}";

    @GetMapping(ASSET_PATH_ID)
    public List<Asset> getAsset(@PathVariable("customerId") UUID customerId){
        return ResponseEntity.ok().body(assetService.getAssetById(customerId)).getBody();
    }

    @GetMapping(ASSET_PATH)
    public Iterable<Asset> getAllAsset(){
        return ResponseEntity.ok().body(this.assetService.getAllAsset()).getBody();
    }

}
