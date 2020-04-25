package com.himorfosis.moneymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AccountIncorrectException extends RuntimeException {

    public AccountIncorrectException() {
        super("Incorrect Email or Password. Please Try Again ");
    }

}
