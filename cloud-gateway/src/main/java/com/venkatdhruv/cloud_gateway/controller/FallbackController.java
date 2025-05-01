package com.venkatdhruv.cloud_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/order-service")
    public String orderServiceFallback() {
        return "Order service is down. Please try again later. ";
    }
    
    @GetMapping("/product-service") 
    public String productServiceFallback() {
        return "Product service is down. Please try again later. ";
    }

    @GetMapping("payment-service")
    public String paymentServiceFallback() {
        return "Payment service is down. Please try again later. ";
    }
    
    
}
