package com.himorfosis.moneymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DataNotCompleteException extends RuntimeException{
    public DataNotCompleteException() {
        super("Please complete the data");
    }
}
