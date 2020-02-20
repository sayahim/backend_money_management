package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
