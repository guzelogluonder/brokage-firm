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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private AssetService assetService;

    @Mock
    private AssetMapper assetMapper;


    @InjectMocks
    private OrderService orderService;

    private Asset tryAsset;
    private Asset stockAsset;
    private OrderDto buyRequestOrder;
    private OrderDto sellRequestOrder;
    private Order pendingOrder;
    private OrderDto canceledOrder;

    @BeforeEach
    void setUp() {

        tryAsset = Asset.builder()
                .id(UUID.randomUUID())
                .customerId(1L)
                .assetName("TRY")
                .size(BigDecimal.valueOf(100000))
                .usableSize(BigDecimal.valueOf(100000))
                .build();


        stockAsset = Asset.builder()
                .id(UUID.randomUUID())
                .customerId(1L)
                .assetName("APPL")
                .size(BigDecimal.valueOf(100))
                .usableSize(BigDecimal.valueOf(100))
                .build();


        pendingOrder = new Order();

        buyRequestOrder = OrderDto.builder()
                .orderId(UUID.randomUUID())
                .customerId(1L)
                .assetName("APPL")
                .orderSide(Side.BUY)
                .price(BigDecimal.valueOf(150))
                .size(BigDecimal.valueOf(10))
                .status(Status.PENDING)
                .createDate(LocalDateTime.now())
                .build();


        OrderDto canceledOrder = OrderDto.builder()
                .orderId(UUID.randomUUID())
                .customerId(1L)
                .assetName("APPL")
                .orderSide(Side.BUY)
                .price(BigDecimal.valueOf(150))
                .size(BigDecimal.valueOf(10))
                .status(Status.CANCELED)
                .createDate(LocalDateTime.now())
                .build();

        sellRequestOrder = OrderDto.builder()
                .orderId(UUID.randomUUID())
                .customerId(1L)
                .assetName("APPL")
                .orderSide(Side.SELL)
                .price(BigDecimal.valueOf(150))
                .size(BigDecimal.valueOf(5))
                .status(Status.PENDING)
                .createDate(LocalDateTime.now())
                .build();


        pendingOrder = Order.builder()
                .orderId(UUID.randomUUID())
                .customerId(1L)
                .assetName("APPL")
                .orderSide(Side.BUY)
                .price(BigDecimal.valueOf(150))
                .size(BigDecimal.valueOf(10))
                .status(Status.PENDING)
                .createDate(LocalDateTime.now())
                .build();


    }


    @Test
    void testCreateNewBuyOrder() {

        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), eq("TRY")))
                .thenReturn(Optional.of(tryAsset));

        when(orderMapper.orderDtoToOrder(any(OrderDto.class)))
                .thenReturn(pendingOrder);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(pendingOrder);

        when(orderMapper.orderToDto(any(Order.class)))
                .thenReturn(buyRequestOrder);

        OrderDto response = orderService.createNewOrder(buyRequestOrder);

        assertNotNull(response);
        assertEquals(buyRequestOrder.getOrderId(), response.getOrderId());
        assertEquals(Status.PENDING, response.getStatus());
        verify(assetRepository, times(1)).save(any(Asset.class));
        verify(orderRepository, times(1)).save(any(Order.class));


    }

    @Test
    void createBuyOrderInsufficientBalance() {
        tryAsset.setUsableSize(BigDecimal.valueOf(500));
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), eq("TRY")))
                .thenReturn(Optional.of(tryAsset));

        assertThrows(InsufficientBalanceException.class, () ->
                orderService.createNewOrder(buyRequestOrder));
    }

    @Test
    void createBuyOrderTryAssetNotFound() {
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), eq("TRY")))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                orderService.createNewOrder(buyRequestOrder));
    }

    @Test
    void testCreateNewSellOrder() {

        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), eq("APPL")))
                .thenReturn(Optional.of(stockAsset));

        when(orderMapper.orderDtoToOrder(any(OrderDto.class)))
                .thenReturn(pendingOrder);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(pendingOrder);

        when(orderMapper.orderToDto(any(Order.class)))
                .thenReturn(sellRequestOrder);

        OrderDto response = orderService.createNewOrder(sellRequestOrder);

        assertNotNull(response);
        assertEquals(sellRequestOrder.getOrderId(), response.getOrderId());
        assertEquals(Status.PENDING, response.getStatus());
        verify(assetRepository, times(1)).save(any(Asset.class));
        verify(orderRepository, times(1)).save(any(Order.class));


    }

    @Test
    void createSellOrderInsufficientBalance() {
        stockAsset.setUsableSize(BigDecimal.valueOf(2));
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), eq(stockAsset.getAssetName())))
                .thenReturn(Optional.of(stockAsset));

        assertThrows(InsufficientBalanceException.class, () ->
                orderService.createNewOrder(sellRequestOrder));
    }


    @Test
    void createSellOrderTryAssetNotFound() {
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), eq(sellRequestOrder.getAssetName())))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                orderService.createNewOrder(sellRequestOrder));
    }


    @Test
    void testCancelBuyOrder() {
        UUID testOrderId = UUID.randomUUID();
        pendingOrder.setOrderId(testOrderId);

        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), eq("TRY")))
                .thenReturn(Optional.of(tryAsset));

        when(orderRepository.findById(testOrderId))
                .thenReturn(Optional.of(pendingOrder));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(pendingOrder);

        when(orderMapper.orderToDto(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    return OrderDto.builder()
                            .orderId(order.getOrderId())
                            .customerId(order.getCustomerId())
                            .assetName(order.getAssetName())
                            .orderSide(order.getOrderSide())
                            .price(order.getPrice())
                            .size(order.getSize())
                            .status(order.getStatus())
                            .createDate(order.getCreateDate())
                            .build();
                });

        orderService.deleteOrder(Status.CANCELED, testOrderId);

        verify(orderRepository, times(1)).findById(testOrderId);
        verify(assetRepository, times(1)).save(any(Asset.class));
        verify(orderRepository, times(1)).save(any(Order.class));

    }

    @Test
    void testCancelSellOrder() {
        UUID testOrderId = UUID.randomUUID();
        pendingOrder.setOrderId(testOrderId);
        pendingOrder.setOrderSide(Side.SELL);
        when(assetRepository.findByCustomerIdAndAssetName(sellRequestOrder.getCustomerId(), sellRequestOrder.getAssetName()))
                .thenReturn(Optional.of(stockAsset));

        when(orderRepository.findById(testOrderId))
                .thenReturn(Optional.of(pendingOrder));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(pendingOrder);

        when(orderMapper.orderToDto(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    return OrderDto.builder()
                            .orderId(order.getOrderId())
                            .customerId(order.getCustomerId())
                            .assetName(order.getAssetName())
                            .orderSide(order.getOrderSide())
                            .price(order.getPrice())
                            .size(order.getSize())
                            .status(order.getStatus())
                            .createDate(order.getCreateDate())
                            .build();
                });

        orderService.deleteOrder(Status.CANCELED, testOrderId);

        verify(orderRepository, times(1)).findById(testOrderId);
        verify(assetRepository, times(1)).save(any(Asset.class));
        verify(orderRepository, times(1)).save(any(Order.class));

    }

    @Test
    void testCancelOrderNotFound() {
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                orderService.deleteOrder(Status.CANCELED, UUID.randomUUID()));
    }

    @Test
    void testCancelOrderInvalidStatus() {
        pendingOrder.setStatus(Status.MATCHED);
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(pendingOrder));

        assertThrows(InvalidOrderStatusException.class, () ->
                orderService.deleteOrder(null, UUID.randomUUID()));
    }

}