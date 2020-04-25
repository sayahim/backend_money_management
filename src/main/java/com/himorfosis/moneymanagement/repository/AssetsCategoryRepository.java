package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.AssetsCategoryEntity;
import com.himorfosis.moneymanagement.entity.AssetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetsCategoryRepository extends JpaRepository<AssetsCategoryEntity, Long> {
}
