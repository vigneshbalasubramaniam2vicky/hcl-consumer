package com.example.paymentprocessor.util;

import com.example.paymentprocessor.dto.PaymentOutcomeDto;
import com.example.paymentprocessor.entity.PaymentOutcome;

public final class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentOutcomeDto toDto(PaymentOutcome paymentOutcome) {
        return PaymentOutcomeDto.builder()
                .paymentId(paymentOutcome.getPaymentId())
                .debitAccountId(paymentOutcome.getDebitAccountId())
                .creditAccountId(paymentOutcome.getCreditAccountId())
                .amount(paymentOutcome.getAmount())
                .currency(paymentOutcome.getCurrency())
                .status(paymentOutcome.getStatus())
                .processedAt(paymentOutcome.getProcessedAt())
                .processingTimeMs(paymentOutcome.getProcessingTimeMs())
                .build();
    }
}
