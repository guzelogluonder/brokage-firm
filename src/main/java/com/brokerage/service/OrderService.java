package com.brokerage.service;

import com.brokerage.dto.OrderDto;
import com.brokerage.enums.Side;
import com.brokerage.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    public OrderDto createNewOrder(OrderDto order){

        OrderDto newOrder = new OrderDto();

        newOrder = OrderDto.builder()
                .customerId(order.getCustomerId())
                .orderSide(order.getOrderSide())
                .assetName(order.getAssetName())
                .price(order.getPrice())
                .orderSide(Side.BUY)
                .status(Status.PENDING)
                .build();
        return newOrder;
    }

}
