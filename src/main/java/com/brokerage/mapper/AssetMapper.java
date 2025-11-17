package com.brokerage.mapper;

import com.brokerage.dto.AssetDto;
import com.brokerage.model.Asset;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    Asset assetToDto (AssetDto assetDto);

    AssetDto assetDtoToAsset(Asset asset);

}
