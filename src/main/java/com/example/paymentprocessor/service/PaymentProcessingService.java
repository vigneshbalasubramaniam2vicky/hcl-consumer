package com.example.paymentprocessor.service;

import com.example.paymentprocessor.dto.PaymentOutcomeDto;
import com.example.paymentprocessor.dto.PaymentSubmittedEvent;
import com.example.paymentprocessor.entity.MetricsSummaryEntity;
import com.example.paymentprocessor.entity.PaymentOutcome;
import com.example.paymentprocessor.exception.ResourceNotFoundException;
import com.example.paymentprocessor.metrics.InMemoryMetricsTracker;
import com.example.paymentprocessor.repository.MetricsSummaryRepository;
import com.example.paymentprocessor.repository.PaymentOutcomeRepository;
import com.example.paymentprocessor.util.PaymentMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentProcessingService {

    private final PaymentOutcomeRepository paymentOutcomeRepository;
    private final MetricsSummaryRepository metricsSummaryRepository;
    private final InMemoryMetricsTracker metricsTracker;

    public PaymentProcessingService(PaymentOutcomeRepository paymentOutcomeRepository,
                                    MetricsSummaryRepository metricsSummaryRepository,
                                    InMemoryMetricsTracker metricsTracker) {
        this.paymentOutcomeRepository = paymentOutcomeRepository;
        this.metricsSummaryRepository = metricsSummaryRepository;
        this.metricsTracker = metricsTracker;
    }

    public PaymentOutcome processPayment(PaymentSubmittedEvent event, long processingTimeMs) {
        log.info("Processing payment: {}", event.getPaymentId());
        String status = determineStatus(event);
        PaymentOutcome paymentOutcome = PaymentOutcome.builder()
                .paymentId(event.getPaymentId() == null ? UUID.randomUUID().toString() : event.getPaymentId())
                .debitAccountId(event.getDebitAccountId())
                .creditAccountId(event.getCreditAccountId())
                .amount(event.getAmount())
                .currency(event.getCurrency())
                .status(status)
                .processedAt(Instant.now())
                .processingTimeMs(processingTimeMs)
                .build();
        PaymentOutcome saved = paymentOutcomeRepository.save(paymentOutcome);
        metricsTracker.incrementStatus(status, processingTimeMs);
        persistMetricsSummary();
        return saved;
    }

    public Page<PaymentOutcomeDto> getActivity(String status, String accountId, Pageable pageable) {
        Page<PaymentOutcome> page;
        if (status != null && accountId != null) {
            page = paymentOutcomeRepository.findByStatusAndDebitAccountIdOrStatusAndCreditAccountIdOrderByProcessedAtDesc(
                    status, accountId, status, accountId, pageable);
        } else if (status != null) {
            page = paymentOutcomeRepository.findByStatusOrderByProcessedAtDesc(status, pageable);
        } else if (accountId != null) {
            page = paymentOutcomeRepository.findByDebitAccountIdOrCreditAccountIdOrderByProcessedAtDesc(accountId, accountId, pageable);
        } else {
            page = paymentOutcomeRepository.findAll(pageable);
        }
        return page.map(PaymentMapper::toDto);
    }

    public Page<PaymentOutcomeDto> getAccountHistory(String accountId, Pageable pageable) {
        return paymentOutcomeRepository
                .findByDebitAccountIdOrCreditAccountIdOrderByProcessedAtDesc(accountId, accountId, pageable)
                .map(PaymentMapper::toDto);
    }

    public PaymentOutcomeDto getById(String paymentId) {
        return paymentOutcomeRepository.findById(paymentId)
                .map(PaymentMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Payment outcome not found: " + paymentId));
    }

    private void persistMetricsSummary() {
        var summary = metricsTracker.summary();
        metricsSummaryRepository.save(MetricsSummaryEntity.builder()
                .id("singleton")
                .totalProcessed(summary.getTotalProcessed())
                .totalHeld(summary.getTotalHeld())
                .totalRejected(summary.getTotalRejected())
                .avgProcessingTimeMs(summary.getAvgProcessingTimeMs())
                .updatedAt(Instant.now())
                .build());
    }

    private String determineStatus(PaymentSubmittedEvent event) {
        if (event.getAmount() != null && event.getAmount().compareTo(new BigDecimal("250000")) > 0) {
            return "HELD";
        }
        if (event.getDebitAccountId() != null && event.getDebitAccountId().equals(event.getCreditAccountId())) {
            return "REJECTED";
        }
        return "PROCESSED";
    }
}
