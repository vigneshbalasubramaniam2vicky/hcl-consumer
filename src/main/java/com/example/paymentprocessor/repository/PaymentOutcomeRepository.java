package com.example.paymentprocessor.repository;

import com.example.paymentprocessor.entity.PaymentOutcome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentOutcomeRepository extends MongoRepository<PaymentOutcome, String> {
    Page<PaymentOutcome> findByStatusOrderByProcessedAtDesc(String status, Pageable pageable);

    Page<PaymentOutcome> findByDebitAccountIdOrCreditAccountIdOrderByProcessedAtDesc(
            String debitAccountId, String creditAccountId, Pageable pageable);

    Page<PaymentOutcome> findByStatusAndDebitAccountIdOrStatusAndCreditAccountIdOrderByProcessedAtDesc(
            String status1, String debitAccountId, String status2, String creditAccountId, Pageable pageable);
}
