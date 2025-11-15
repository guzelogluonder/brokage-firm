package com.brokerage.controller;

import com.brokerage.dto.OrderDto;
import com.brokerage.enums.Status;
import com.brokerage.model.Order;
import com.brokerage.service.OrderService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    public static final String API_ORDERS = "/api/orders";
    public static final String DELETE_ORDER_PATH = "/api/orders/{orderId}";

    @PostMapping(API_ORDERS)
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){

        return new ResponseEntity<>(orderService.createNewOrder(orderDto), HttpStatus.CREATED);

    }

    @GetMapping(API_ORDERS)
    public List<Order> listOrders (@RequestParam Long customerId,
                                   @RequestParam(required = false) LocalDateTime startDate,
                                   @RequestParam(required = false) LocalDateTime endDate){
        return ResponseEntity.ok().body(orderService.listOrders(customerId,startDate,endDate)).getBody();
    }

    @DeleteMapping(DELETE_ORDER_PATH)
    public Boolean deleteOrder(@PathVariable("orderId") UUID orderId){
        orderService.deleteOrder(Status.CANCELED,orderId);
        return true;
    }
}
