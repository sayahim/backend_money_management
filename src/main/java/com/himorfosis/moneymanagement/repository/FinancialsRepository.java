package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.FinancialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialsRepository extends JpaRepository<FinancialEntity, String> {
}
