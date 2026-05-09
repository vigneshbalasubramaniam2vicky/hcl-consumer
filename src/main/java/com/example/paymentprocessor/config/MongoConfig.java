package com.example.paymentprocessor.config;

import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoConfig {

    private final MongoTemplate mongoTemplate;

    public MongoConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void createCollectionsIfMissing() {
        List<String> collections = List.of("payment_outcomes", "accounts", "metrics_summary");
        collections.stream().filter(name -> !mongoTemplate.collectionExists(name)).forEach(mongoTemplate::createCollection);
    }
}
