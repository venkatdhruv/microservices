package com.venkatdhruv.order_service.exception;

import lombok.Data;

@Data
public class OrderCustomException extends RuntimeException {

    private String errorCode;
    private int status;

    public OrderCustomException(String message, String errorCodeString, int status) {
        super(message);
        this.errorCode = errorCodeString;
        this.status = status;
    }
}
