package com.venkatdhruv.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.netflix.spectator.impl.PatternExpr.Or;
import com.venkatdhruv.order_service.decoder.PaymentRequest;
import com.venkatdhruv.order_service.entity.Order;
import com.venkatdhruv.order_service.exception.OrderCustomException;
import com.venkatdhruv.order_service.model.PaymentResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "payment-service/payment")
public interface PaymentService {

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

     @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetailsByOrderId(@PathVariable("orderId") Long orderId);

    default ResponseEntity<OrderCustomException> fallback(Exception e) {
        OrderCustomException orderCustomException = new OrderCustomException("Payment service is down", e.getMessage(), 500);
        return ResponseEntity.status(500).body(orderCustomException);
    }

}
