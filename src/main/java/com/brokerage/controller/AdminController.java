package com.brokerage.controller;

import com.brokerage.dto.OrderDto;
import com.brokerage.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
public class AdminController {
    private final OrderService orderService;

    @PostMapping("/api/match-order")
    public ResponseEntity<OrderDto> matchOrder(@RequestBody OrderDto order) {
        OrderDto response = orderService.matchOrder(order.getOrderId());
        return ResponseEntity.ok(response);
}


}
