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
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){

      OrderDto newOrder = orderService.createNewOrder(orderDto);

        return ResponseEntity.ok().body(newOrder);

    }

}
