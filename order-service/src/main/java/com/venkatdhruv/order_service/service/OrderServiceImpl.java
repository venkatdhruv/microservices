package com.venkatdhruv.order_service.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.venkatdhruv.order_service.client.PaymentService;
import com.venkatdhruv.order_service.client.ProductService;
import com.venkatdhruv.order_service.decoder.PaymentRequest;
import com.venkatdhruv.order_service.entity.Order;
import com.venkatdhruv.order_service.exception.OrderCustomException;
import com.venkatdhruv.order_service.model.OrderRequest;
import com.venkatdhruv.order_service.model.OrderResponse;
import com.venkatdhruv.order_service.model.PaymentResponse;
import com.venkatdhruv.order_service.model.ProductDetails;
import com.venkatdhruv.order_service.repository.OrderRepository;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

   
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService  paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Long placeOrder(@NonNull OrderRequest orderRequest) {

        log.info(null != orderRequest ? "Order request received: " + orderRequest : "Order request is null");

        // Order entity -  save the data with status as created
        // product service -  reduce the quantity of the product by the order quantity
        // payment service -  initiate the payment with the order amount and payment mode -> success else cancel

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
        log.info("Product quantity reduced successfully for product ID: {}", orderRequest.getProductId());
        log.info("Placing order with details: {}", orderRequest);
        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .amount(orderRequest.getTotalAmount())
                .quantity(orderRequest.getQuantity())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .build();
            
        order = orderRepository.save(order);
        log.info("Order placed successfully with ID: {}", order.getId());

        log.info("Initiating payment for order ID: {}", order.getId());
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .amount(orderRequest.getTotalAmount())
                .paymentMode(orderRequest.getPaymentMode())
                .build();

        String orderStatus = null;

        try {
            paymentService.doPayment(paymentRequest);
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Payment failed for order ID: {}. Error: {}", order.getId(), e.getMessage());
            orderStatus = "PAYMENT_FAILED";
        }
        log.info("Payment status for order ID {}: {}", order.getId(), orderStatus);
        order.setOrderStatus(orderStatus);
        return order.getId();
    }


    @Override
    public OrderResponse getOrderDetails(Long orderId) {
        log.info("Fetching order details for order ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderCustomException("Order not found with ID: " + orderId, "ORDER_NOT_FOUND", 404));

        log.info("calling product service to get the product dtails for order ID: {}", orderId);
        ProductDetails productDetails = restTemplate.getForObject("http://product-service/product/"+ order.getProductId(), ProductDetails.class);
        log.info("Order details fetched successfully for order ID: {}", orderId);

        PaymentResponse paymentResponse = paymentService.getPaymentDetailsByOrderId(orderId).getBody();

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .productDetails(productDetails)
                .paymentDetails(paymentResponse)
                .build();
        return orderResponse;
    }

    

}
