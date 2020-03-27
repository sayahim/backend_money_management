package com.himorfosis.moneymanagement.service;

import com.himorfosis.moneymanagement.entity.UsersEntity;
import com.himorfosis.moneymanagement.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SecurityUserDetailService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UsersEntity user = usersRepo.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

}
