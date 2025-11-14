package com.brokerage.mapper;

import com.brokerage.dto.AssetDto;
import com.brokerage.dto.OrderDto;
import com.brokerage.model.Asset;
import com.brokerage.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    Asset assetToDto (AssetDto assetDto);

    AssetDto assetDtoToAsset(Asset asset);

}
