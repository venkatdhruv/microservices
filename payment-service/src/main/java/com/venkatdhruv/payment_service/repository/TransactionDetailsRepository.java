package com.venkatdhruv.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venkatdhruv.payment_service.entity.TransactionDetails;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> {
    // Custom query methods can be defined here if needed

    TransactionDetails findByOrderId(Long orderId);

}
