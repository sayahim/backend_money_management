package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.AssetsEntity;
import com.himorfosis.moneymanagement.entity.FinancialEntity;
import com.himorfosis.moneymanagement.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssetsRepository extends JpaRepository<AssetsEntity, Long> {

    @Query(value = "SELECT * FROM assets WHERE id_assets_category =:id ", nativeQuery = true)
    List<AssetsEntity> findAllAssetsCategoryId(String id);

}
