# payment-processor

Spring Boot 3 microservice for payment decisioning and Kafka-based event processing.

## Features
- Kafka consumer (`payments.submitted`) and producer (`payments.processed`)
- MongoDB persistence with indexed `payment_outcomes`
- Business rules: HELD, REJECTED, PROCESSED
- REST reporting APIs with pagination/filtering
- In-memory metrics counters via `AtomicLong`
- Actuator health endpoint and Swagger UI

## Run
```bash
mvn clean install
mvn spring-boot:run
```

## APIs
- `GET /api/metrics/summary`
- `GET /api/reports/summary?page=0&size=10`
- `GET /api/reports/activity?status=HELD&accountId=123&page=0&size=10`
- `GET /api/accounts/{accountId}/history?page=0&size=10`
- `GET /actuator/health`

## Curl examples
```bash
curl http://localhost:8084/api/metrics/summary
curl "http://localhost:8084/api/reports/summary?page=0&size=10"
curl "http://localhost:8084/api/reports/activity?status=HELD&accountId=123&page=0&size=10"
curl "http://localhost:8084/api/accounts/20-15-88-43917265/history?page=0&size=10"
curl http://localhost:8084/actuator/health
```

Swagger: `http://localhost:8084/swagger-ui.html`
