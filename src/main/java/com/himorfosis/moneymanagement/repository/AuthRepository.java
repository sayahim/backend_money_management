package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthRepository extends JpaRepository<UsersEntity, Long> {

//    @Query(value = "SELECT * FROM users WHERE email :email", nativeQuery = true)
//    List<UsersEntity> checkEmailUser(
//            @Param("email")String email);

    UsersEntity findByEmail(String username);


//    @Query(value = "SELECT * FROM users WHERE email :email", nativeQuery = true)
//    public UsersEntity findByEmail(String email);

}
