package com.venkatdhruv.order_service.service;

import com.venkatdhruv.order_service.model.OrderRequest;
import com.venkatdhruv.order_service.model.OrderResponse;

public interface OrderService {

    Long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(Long orderId);

}
