package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query(value = "SELECT * FROM category WHERE updated_at = ?1", nativeQuery = true)
    List<CategoryEntity> findByDate(String updatedAt);

    @Query(value = "SELECT * FROM category WHERE updated_at BETWEEN :dateStart AND :dateFinish", nativeQuery = true)
    List<CategoryEntity> sortDateBy(@Param("dateStart") String dateStart, @Param("dateFinish")  String dateFinish);

}
