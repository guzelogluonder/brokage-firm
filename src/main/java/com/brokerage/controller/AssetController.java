package com.brokerage.controller;

import com.brokerage.model.Asset;
import com.brokerage.service.AssetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class AssetController {

    private final AssetService assetService;

    public static final String ASSET_PATH = "/assets";
    public static final String ASSET_PATH_ID = "/assets/{customerId}";

    @GetMapping(ASSET_PATH_ID)
    public List<Asset> getAsset(@PathVariable("customerId") UUID customerId){
        return ResponseEntity.ok().body(assetService.getAssetById(customerId)).getBody();
    }

    @GetMapping(ASSET_PATH)
    public Iterable<Asset> getAllAssets(){
        return ResponseEntity.ok().body(this.assetService.getAllAsset()).getBody();
    }

}
