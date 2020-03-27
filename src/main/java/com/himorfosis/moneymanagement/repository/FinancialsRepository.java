package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.entity.FinancialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FinancialsRepository extends JpaRepository<FinancialEntity, Long> {

    @Query(value = "SELECT * FROM financials WHERE id_user = ?1", nativeQuery = true)
    List<FinancialEntity> findAllFinancialUsers(String userId);


}
