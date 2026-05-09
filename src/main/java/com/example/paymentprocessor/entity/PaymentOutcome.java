package com.example.paymentprocessor.entity;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payment_outcomes")
@CompoundIndexes({
        @CompoundIndex(name = "status_processedAt_idx", def = "{'status': 1, 'processedAt': -1}"),
        @CompoundIndex(name = "debitAccountId_processedAt_idx", def = "{'debitAccountId': 1, 'processedAt': -1}"),
        @CompoundIndex(name = "creditAccountId_processedAt_idx", def = "{'creditAccountId': 1, 'processedAt': -1}")
})
public class PaymentOutcome {

    @Id
    private String paymentId;

    @Indexed
    private String debitAccountId;

    @Indexed
    private String creditAccountId;

    private BigDecimal amount;
    private String currency;
    @Indexed
    private String status;
    private Instant processedAt;
    private long processingTimeMs;
}
