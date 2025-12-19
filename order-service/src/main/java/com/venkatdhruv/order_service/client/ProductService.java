package com.venkatdhruv.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.venkatdhruv.order_service.exception.OrderCustomException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "product-service", url = "${microservice.product-service.base-url}")
public interface ProductService {

    @PutMapping("reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId, @RequestParam("quantity") long quantity);

    default ResponseEntity<OrderCustomException> fallback(Exception e) {
        OrderCustomException orderCustomException = new OrderCustomException("Product service is down", e.getMessage(), 500);
        return ResponseEntity.status(500).body(orderCustomException);
    }
}
