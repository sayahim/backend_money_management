package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<UsersEntity, String> {

    UsersEntity findByEmail(String email);
}
