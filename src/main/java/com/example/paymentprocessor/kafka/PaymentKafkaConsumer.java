package com.example.paymentprocessor.kafka;

import com.example.paymentprocessor.dto.PaymentOutcomeDto;
import com.example.paymentprocessor.dto.PaymentSubmittedEvent;
import com.example.paymentprocessor.service.PaymentProcessingService;
import com.example.paymentprocessor.util.PaymentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
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
    public void consume(PaymentSubmittedEvent event,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        @Header(KafkaHeaders.OFFSET) long offset) {
        long start = System.currentTimeMillis();
        log.info("Received payment message key={} partition={} offset={} paymentId={} amount={} {}",
                key, partition, offset, event.getPaymentId(), event.getAmount(), event.getCurrency());

        var saved = paymentProcessingService.processPayment(event, System.currentTimeMillis() - start);
        PaymentOutcomeDto dto = PaymentMapper.toDto(saved);

        kafkaTemplate.send("payments.processed", dto.getPaymentId(), dto);
        log.info("Published processed event paymentId={} status={}", dto.getPaymentId(), dto.getStatus());
    }

    @KafkaListener(topics = "payments.submitted.DLT", groupId = "payment-processor-group-dlt")
    public void consumeDlt(PaymentSubmittedEvent event,
                           @Header(KafkaHeaders.RECEIVED_KEY) String key,
                           @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                           @Header(KafkaHeaders.OFFSET) long offset) {
        log.error("DLT message received key={} partition={} offset={} payload={}", key, partition, offset, event);
    }
}
