package com.venkatdhruv.payment_service.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venkatdhruv.payment_service.entity.TransactionDetails;
import com.venkatdhruv.payment_service.model.PaymentMode;
import com.venkatdhruv.payment_service.model.PaymentRequest;
import com.venkatdhruv.payment_service.model.PaymentResponse;
import com.venkatdhruv.payment_service.repository.TransactionDetailsRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Payment request received: {}", paymentRequest);

        TransactionDetails transactionDetails = TransactionDetails.builder()
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentDate(Instant.now())
                .paymentStatus("SUCCESS") // Assuming payment is successful for this example
                .build();
        log.info("Transaction details: {}", transactionDetails);

        transactionDetails = transactionDetailsRepository.save(transactionDetails);
        log.info("Transaction details saved to database: {}", transactionDetails);

        return transactionDetails.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(Long orderId) {
        log.info("payment details for the order id : {}", orderId);
        TransactionDetails transactionDetails = transactionDetailsRepository.findByOrderId(orderId);
        log.info("Transaction details retrieved from database: {}", transactionDetails);

        if (transactionDetails == null) {
            log.error("No payment details found for order ID: {}", orderId);
            return null; // or throw an exception
        }

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .orderId(transactionDetails.getOrderId())
                .status(transactionDetails.getPaymentStatus())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .amount(transactionDetails.getAmount())
                .paymentDate(transactionDetails.getPaymentDate())
                .build();

        log.info("Payment response: {}", paymentResponse);
        return paymentResponse;

    }

}
