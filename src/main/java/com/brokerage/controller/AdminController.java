package com.brokerage.controller;

import com.brokerage.service.AssetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
public class AdminController {
    private final AssetService assetService;

}
