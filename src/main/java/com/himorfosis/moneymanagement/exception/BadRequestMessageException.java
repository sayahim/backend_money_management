package com.himorfosis.moneymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestMessageException  extends RuntimeException {
    public BadRequestMessageException(String msg) {
        super(msg);
    }
}
