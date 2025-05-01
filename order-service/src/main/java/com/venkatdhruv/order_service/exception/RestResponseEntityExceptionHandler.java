package com.venkatdhruv.order_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.venkatdhruv.order_service.decoder.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OrderCustomException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceCustomException(OrderCustomException ex) {
        com.venkatdhruv.order_service.decoder.ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getStatus() == 0 ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.valueOf(ex.getStatus()));
    }
}
