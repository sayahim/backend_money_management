package com.himorfosis.moneymanagement.security.jwt;

import com.himorfosis.moneymanagement.entity.UsersEntity;
import com.himorfosis.moneymanagement.repository.AuthRepository;
import com.himorfosis.moneymanagement.repository.UsersRepository;
import com.himorfosis.moneymanagement.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JwtSecurityDetailService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;
    private String TAG = "JwtSecurityDetailService";

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UsersEntity user = usersRepository.findByEmail(s);

        if (user == null) {
            isLog("User not found with email = " + s);
            throw new UsernameNotFoundException("User not found with email: " + s);
        }
        return new User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

    private void isLog(String message) {
        Util.log(TAG, message);
    }
}
