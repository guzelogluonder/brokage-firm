package com.brokerage.service;

import com.brokerage.dto.OrderDto;
import com.brokerage.enums.Side;
import com.brokerage.enums.Status;
import com.brokerage.exception.InsufficientBalanceException;
import com.brokerage.exception.InvalidOrderStatusException;
import com.brokerage.exception.ResourceNotFoundException;
import com.brokerage.mapper.AssetMapper;
import com.brokerage.mapper.OrderMapper;
import com.brokerage.model.Asset;
import com.brokerage.model.Order;
import com.brokerage.repository.AssetRepository;
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
    private final AssetRepository assetRepository;

    @Transactional
    public OrderDto createNewOrder(OrderDto order) {

        OrderDto newOrder = OrderDto.builder()
                .customerId(order.getCustomerId())
                .orderSide(order.getOrderSide())
                .assetName(order.getAssetName())
                .size(order.getSize())
                .price(order.getPrice())
                .status(Status.PENDING)
                .createDate(LocalDateTime.now())
                .build();

        if (order.getOrderSide().equals(Side.BUY)) {
            handleBuyAsset(newOrder);
        } else {
            handleSellAsset(newOrder);
        }

     Order savedOrder = orderRepository.save(orderMapper.orderDtoToOrder(newOrder));

        return orderMapper.orderToDto(savedOrder);
    }

    public List<Order> listOrders(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return orderRepository.findOrdersByCustomerAndDateRange(customerId, startDate, endDate);
        } else {
            return orderRepository.findByCustomerId(customerId);
        }
    }

    public void deleteOrder(Status status, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() != Status.PENDING) {
            throw new InvalidOrderStatusException("Only PENDING orders can be deleted. Current status: " + order.getStatus());
        }

        if (order.getOrderSide() == Side.BUY) {
            releaseBuyOrderAssets(order);
        } else {
            releaseSellOrderAssets(order);
        }

        order.setStatus(status);
        orderRepository.save(order);
        orderMapper.orderToDto(order);
    }


    public void handleBuyAsset(OrderDto order) {
        Asset asset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "TRY")
                .orElseThrow(() -> new ResourceNotFoundException("Asset " + order.getAssetName() + " not found for customer: " + order.getCustomerId()));

        BigDecimal totalPrice = order.getSize().multiply(order.getPrice());

        if (asset.getUsableSize().compareTo(totalPrice) < 0) {
            throw new InsufficientBalanceException("Insufficient TRY balance. Required: " + order.getSize() + ", Available: " + asset.getUsableSize());
        }

        asset.setUsableSize(asset.getUsableSize().subtract(totalPrice));
        assetRepository.save(asset);
    }

    public void handleSellAsset(OrderDto order) {
        Asset asset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                .orElseThrow(() -> new ResourceNotFoundException("Asset " + order.getAssetName() + " not found for customer: " + order.getCustomerId()));

        if (asset.getUsableSize().compareTo(order.getSize()) < 0) {
            throw new InsufficientBalanceException("Insufficient " + order.getAssetName() + " balance. Required: " + order.getSize() + ", Available: " + asset.getUsableSize());
        }
        asset.setUsableSize(asset.getUsableSize().subtract(order.getSize()));

        assetRepository.save(asset);
    }

    public void releaseBuyOrderAssets(Order order) {

        Asset asset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "TRY")
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + order.getCustomerId()));

        BigDecimal totalRelease = order.getSize().multiply(order.getPrice());
        asset.setUsableSize(asset.getUsableSize().add(totalRelease));
        assetRepository.save(asset);
    }

    public void releaseSellOrderAssets(Order order) {
        Asset asset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + order.getCustomerId()));

        asset.setUsableSize(asset.getUsableSize().add(order.getSize()));
        assetRepository.save(asset);
    }
}
