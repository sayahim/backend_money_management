package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.entity.FinancialEntity;
import com.himorfosis.moneymanagement.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    @Query(value = "SELECT * FROM users WHERE id :userId", nativeQuery = true)
    UsersEntity checkUserId(
            @Param("userId") Long userId);

    @Query(value = "SELECT * FROM users WHERE id :id", nativeQuery = true)
    UsersEntity findId(@Param("id")Long id);

    @Query(value = "SELECT * FROM users WHERE email :getMail", nativeQuery = true)
    UsersEntity checkEmailUser(
            @Param("getMail")String getMail);

    public UsersEntity findByEmail(String email);
    public UsersEntity findByUsername(String username);

}
