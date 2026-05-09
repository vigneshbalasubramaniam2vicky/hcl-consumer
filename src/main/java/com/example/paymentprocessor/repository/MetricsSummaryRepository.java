package com.example.paymentprocessor.repository;

import com.example.paymentprocessor.entity.MetricsSummaryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MetricsSummaryRepository extends MongoRepository<MetricsSummaryEntity, String> {
}
