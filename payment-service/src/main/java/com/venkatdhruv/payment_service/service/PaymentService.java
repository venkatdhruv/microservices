package com.venkatdhruv.payment_service.service;

import com.venkatdhruv.payment_service.model.PaymentRequest;
import com.venkatdhruv.payment_service.model.PaymentResponse;

public interface PaymentService {

    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(Long orderId);

}
