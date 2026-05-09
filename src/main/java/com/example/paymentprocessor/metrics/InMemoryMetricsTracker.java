package com.example.paymentprocessor.metrics;

import com.example.paymentprocessor.dto.MetricsSummaryResponse;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class InMemoryMetricsTracker {

    private final AtomicLong totalProcessed = new AtomicLong();
    private final AtomicLong totalHeld = new AtomicLong();
    private final AtomicLong totalRejected = new AtomicLong();
    private final AtomicLong totalProcessingTimeMs = new AtomicLong();
    private final AtomicLong totalEvents = new AtomicLong();

    public void incrementStatus(String status, long processingTimeMs) {
        switch (status) {
            case "PROCESSED" -> totalProcessed.incrementAndGet();
            case "HELD" -> totalHeld.incrementAndGet();
            case "REJECTED" -> totalRejected.incrementAndGet();
            default -> {
            }
        }
        totalEvents.incrementAndGet();
        totalProcessingTimeMs.addAndGet(processingTimeMs);
    }

    public MetricsSummaryResponse summary() {
        long count = totalEvents.get();
        return MetricsSummaryResponse.builder()
                .totalProcessed(totalProcessed.get())
                .totalHeld(totalHeld.get())
                .totalRejected(totalRejected.get())
                .avgProcessingTimeMs(count == 0 ? 0 : totalProcessingTimeMs.get() / count)
                .build();
    }
}
