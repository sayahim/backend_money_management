package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.entity.FinancialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FinancialsRepository extends JpaRepository<FinancialEntity, String> {

    @Query(value = "SELECT * FROM financials WHERE id :userId", nativeQuery = true)
    List<FinancialEntity> findAllFinancialUsers(@Param("userId")Long userId);

    @Query(value = "SELECT * FROM financials WHERE id :id", nativeQuery = true)
    FinancialEntity findId(@Param("id")Long id);

}
