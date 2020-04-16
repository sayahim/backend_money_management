package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.entity.UsersEntity;
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

    @Query(value = "SELECT * FROM category WHERE type_category =:getTypeCategory", nativeQuery = true)
    List<CategoryEntity> findByCategoryTypeFinance(@Param("getTypeCategory") String getTypeCategory);

    @Query(value = "SELECT * FROM category WHERE id_user_category  =:user_id", nativeQuery = true)
    List<CategoryEntity> findCategoryUser(String user_id);

    @Query(value = "SELECT * FROM category WHERE id_user_category =:defaultCategory", nativeQuery = true)
    List<CategoryEntity> findCategoryDefault(String defaultCategory);

}
