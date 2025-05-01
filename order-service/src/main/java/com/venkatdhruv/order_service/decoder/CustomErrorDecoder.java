package com.venkatdhruv.order_service.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.venkatdhruv.order_service.exception.OrderCustomException;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("{}", response.request().url());
        log.info("{}", response.request().headers());

        ErrorResponse errorResponse = null;
        try {
            errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);
        } catch (Exception e) {
            log.error("Error while decoding error response", e);
            throw new RuntimeException("Error while decoding error response", e);
        }

        return new OrderCustomException(errorResponse.getErrorMessage(), errorResponse.getErrorCode(), response.status());
    }

}
