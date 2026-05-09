package com.example.paymentprocessor.entity;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "metrics_summary")
public class MetricsSummaryEntity {
    @Id
    private String id;
    private long totalProcessed;
    private long totalHeld;
    private long totalRejected;
    private long avgProcessingTimeMs;
    private Instant updatedAt;
}
