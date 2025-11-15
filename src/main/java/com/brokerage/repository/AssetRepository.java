package com.brokerage.repository;

import com.brokerage.model.Asset;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetRepository extends JpaRepository<Asset, UUID> {

    @Query("SELECT a FROM assets a WHERE a.customerId = :customerId")
    List<Asset> findAllByCustomerId(Long customerId);

    Optional<Asset> findByCustomerIdAndAssetName(Long customerId, String assetName);

    @Transactional
    @Modifying
    @Query("UPDATE assets a SET a.usableSize =:usableSize WHERE a.id = :id")
    void updateUsableSizeById(UUID id, BigDecimal usableSize);
}

