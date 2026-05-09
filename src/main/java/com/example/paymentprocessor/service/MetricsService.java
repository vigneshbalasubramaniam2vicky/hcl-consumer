package com.example.paymentprocessor.service;

import com.example.paymentprocessor.dto.MetricsSummaryResponse;
import com.example.paymentprocessor.metrics.InMemoryMetricsTracker;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final InMemoryMetricsTracker metricsTracker;

    public MetricsService(InMemoryMetricsTracker metricsTracker) {
        this.metricsTracker = metricsTracker;
    }

    public MetricsSummaryResponse getSummary() {
        return metricsTracker.summary();
    }
}
