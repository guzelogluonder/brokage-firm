package com.brokerage.controller;

import com.brokerage.dto.OrderDto;
import com.brokerage.enums.Status;
import com.brokerage.model.Order;
import com.brokerage.service.OrderService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    public static final String ORDER_CREATE_PATH = "/orders/{customerId}";
    public static final String DELETE_ORDER_CREATE_PATH = "/orders/{customerId}/{orderId}";

    @PostMapping(ORDER_CREATE_PATH)
    public ResponseEntity<OrderDto> createOrder(@PathVariable("customerId") UUID customerId, @RequestBody OrderDto orderDto){

        orderDto.setCustomerId(customerId);

        return ResponseEntity.ok().body( orderService.createNewOrder(orderDto));

    }

    @GetMapping(ORDER_CREATE_PATH)
    public List<Order> getOrder(@PathVariable("customerId") UUID customerId){
        return ResponseEntity.ok().body(orderService.getOrder(customerId)).getBody();
    }

    @DeleteMapping(DELETE_ORDER_CREATE_PATH)
    public void deleteOrder(@PathVariable("customerId") UUID customerId, @PathVariable("orderId") UUID orderId){
        orderService.deleteOrder(Status.CANCELED,customerId,orderId);
    }

}
