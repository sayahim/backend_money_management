package com.himorfosis.moneymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MessageException extends RuntimeException {

    public MessageException(String message){
        super(message);
    }

}
