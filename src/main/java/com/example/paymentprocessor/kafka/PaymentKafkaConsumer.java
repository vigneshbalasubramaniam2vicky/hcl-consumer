package com.example.paymentprocessor.kafka;

import com.example.paymentprocessor.dto.PaymentOutcomeDto;
import com.example.paymentprocessor.dto.PaymentSubmittedEvent;
import com.example.paymentprocessor.service.PaymentProcessingService;
import com.example.paymentprocessor.util.PaymentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentKafkaConsumer {

    private final PaymentProcessingService paymentProcessingService;
    private final KafkaTemplate<String, PaymentOutcomeDto> kafkaTemplate;

    public PaymentKafkaConsumer(PaymentProcessingService paymentProcessingService,
                                KafkaTemplate<String, PaymentOutcomeDto> kafkaTemplate) {
        this.paymentProcessingService = paymentProcessingService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payments.submitted", groupId = "payment-processor-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(PaymentSubmittedEvent event) {
        long start = System.currentTimeMillis();
        log.info("Received event paymentId={} debit={} credit={} amount={} currency={}",
                event.getPaymentId(), event.getDebitAccountId(), event.getCreditAccountId(), event.getAmount(), event.getCurrency());

        var saved = paymentProcessingService.processPayment(event, System.currentTimeMillis() - start);
        PaymentOutcomeDto dto = PaymentMapper.toDto(saved);

        kafkaTemplate.send("payments.processed", dto.getPaymentId(), dto);
        log.info("Published processed event paymentId={} status={}", dto.getPaymentId(), dto.getStatus());
    }
}
