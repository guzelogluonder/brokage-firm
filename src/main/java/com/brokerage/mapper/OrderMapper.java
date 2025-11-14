package com.brokerage.mapper;

import com.brokerage.dto.OrderDto;
import com.brokerage.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order orderDtoToOrder (OrderDto orderDto);

    OrderDto orderToDto(Order order);

}
