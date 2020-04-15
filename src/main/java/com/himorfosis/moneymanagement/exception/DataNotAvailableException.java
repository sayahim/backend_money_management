package com.himorfosis.moneymanagement.exception;

import com.himorfosis.moneymanagement.state.MsgState;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DataNotAvailableException extends RuntimeException{
    public DataNotAvailableException() {
        super(MsgState.NOT_AVAIL);
    }
}
