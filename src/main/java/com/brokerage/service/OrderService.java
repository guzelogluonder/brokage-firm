package com.brokerage.service;

import com.brokerage.dto.AssetDto;
import com.brokerage.dto.OrderDto;
import com.brokerage.enums.Side;
import com.brokerage.enums.Status;
import com.brokerage.mapper.OrderMapper;
import com.brokerage.model.Order;
import com.brokerage.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderDto createNewOrder(OrderDto order) {

        OrderDto newOrder = new OrderDto();

        newOrder = OrderDto.builder()
                .customerId(order.getCustomerId())
                .orderSide(order.getOrderSide())
                .assetName(order.getAssetName())
                .size(order.getSize())
                .price(order.getPrice())
                .status(Status.PENDING)
                .createDate(LocalDateTime.now())
                .build();

       Order savedOrder = orderRepository.save(orderMapper.orderDtoToOrder(newOrder));

        return orderMapper.orderToDto(savedOrder);
    }

    public List<Order> getOrder(UUID customerId) {

        List<Order> orders = orderRepository.findAll();

        return orders.stream().filter(order -> order.getCustomerId().equals(customerId)).toList();
    }

    public void deleteOrder(Status status,UUID customerId, UUID orderId) {
         orderRepository.deleteById(status,orderId,customerId);
    }

}
