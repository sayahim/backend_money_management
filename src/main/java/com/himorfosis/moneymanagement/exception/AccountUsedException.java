package com.himorfosis.moneymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AccountUsedException extends RuntimeException {

    public AccountUsedException(String message) {
        super(message + " Has Been Used");
    }

}
