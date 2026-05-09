package com.example.paymentprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricsSummaryResponse {
    private long totalProcessed;
    private long totalHeld;
    private long totalRejected;
    private long avgProcessingTimeMs;
}
