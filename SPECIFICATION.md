# Parcel Tracking System - Technical Specification Document

**Version:** 1.0  
**Date:** November 27, 2025  
**Project:** Parcel Tracking Application  
**Technology Stack:** Java 21, Spring Boot 3.3+, PostgreSQL

---

## 1. Executive Summary

This technical specification document defines the architecture, design, and implementation details for the Parcel Tracking System (PTS) microservice. The system will be built using Java 21 and Spring Boot 3, following enterprise-grade security, scalability, and maintainability standards.

### 1.1 System Overview

The PTS provides real-time parcel tracking capabilities for customers and administrative functions for managing shipment data. The system exposes a RESTful API following OpenAPI 3.0 specifications.

---

## 2. Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 21 |
| **Framework** | Spring Boot | 3.3+ |
| **Build Tool** | Maven | 3.9+ |
| **Database (Production)** | PostgreSQL | Latest |
| **Database (Dev/Test)** | H2 | Latest |
| **API Documentation** | SpringDoc OpenAPI | 3.0 |
| **Caching** | Caffeine (local) | Latest |
| **Validation** | Jakarta Bean Validation | 3.0 |
| **Testing** | JUnit 5, Mockito | Latest |
| **Container** | Docker | Latest |

---

## 3. System Architecture

### 3.1 Application Structure

```
com/hcl/parceltracking/
├── ParcelTrackingApplication.java
├── config/
│   ├── CacheConfig.java
│   ├── OpenApiConfig.java
│   └── SecurityConfig.java
├── controller/
│   ├── ParcelController.java
│   └── AdminController.java
├── dto/
│   ├── ParcelTrackingRequest.java
│   ├── ParcelTrackingResponse.java
│   ├── ParcelStatusUpdateRequest.java
│   ├── ParcelCreateRequest.java
│   └── ErrorResponse.java
├── model/
│   ├── Parcel.java
│   └── StatusHistory.java
├── repository/
│   ├── ParcelRepository.java
│   └── StatusHistoryRepository.java
├── service/
│   ├── ParcelService.java
│   ├── ParcelServiceImpl.java
│   ├── NotificationService.java
│   └── BarcodeService.java
└── exception/
    ├── ResourceNotFoundException.java
    ├── InvalidTrackingIdException.java
    └── GlobalExceptionHandler.java
```

### 3.2 Layered Architecture

- **Controller Layer**: REST endpoints, input validation, HTTP handling
- **Service Layer**: Business logic, transaction management, DTO-Entity mapping
- **Repository Layer**: Data access using Spring Data JPA
- **Model Layer**: JPA entities representing database tables

---

## 4. API Specification

### 4.1 Base URL

```
/api/v1
```

### 4.2 Customer Endpoints

#### 4.2.1 Track Parcel

**Endpoint:** `GET /api/v1/parcels/track/{trackingId}`

**Description:** Retrieve current status and location of a parcel

**Path Parameters:**
- `trackingId` (String, required): Unique tracking identifier (10-15 alphanumeric characters)

**Success Response (200 OK):**
```json
{
  "trackingId": "TRK1234567890",
  "currentStatus": "IN_TRANSIT",
  "currentLocation": "Dallas Distribution Center",
  "lastUpdateTimestamp": "2025-11-27T10:30:00Z",
  "estimatedDeliveryDate": "2025-11-29",
  "statusHistory": [
    {
      "status": "BOOKED",
      "location": "New York Hub",
      "timestamp": "2025-11-25T08:00:00Z"
    },
    {
      "status": "IN_TRANSIT",
      "location": "Dallas Distribution Center",
      "timestamp": "2025-11-27T10:30:00Z"
    }
  ]
}
```

**Error Responses:**
- `404 Not Found`: Tracking ID not found
- `400 Bad Request`: Invalid tracking ID format

#### 4.2.2 Subscribe to Notifications

**Endpoint:** `POST /api/v1/parcels/{trackingId}/subscribe`

**Description:** Subscribe to email/SMS notifications for a parcel

**Path Parameters:**
- `trackingId` (String, required): Tracking identifier

**Request Body:**
```json
{
  "email": "customer@example.com",
  "phoneNumber": "+1234567890",
  "notificationPreferences": ["EMAIL", "SMS"]
}
```

**Success Response (200 OK):**
```json
{
  "message": "Successfully subscribed to notifications",
  "trackingId": "TRK1234567890"
}
```

### 4.3 Admin Endpoints

#### 4.3.1 Create New Shipment

**Endpoint:** `POST /api/v1/admin/parcels`

**Description:** Create a new parcel shipment with auto-generated barcode

**Request Body:**
```json
{
  "originLocation": "New York Hub",
  "destinationLocation": "Los Angeles Hub",
  "recipientName": "John Doe",
  "recipientAddress": "123 Main St, Los Angeles, CA 90001",
  "estimatedDeliveryDate": "2025-12-01"
}
```

**Success Response (201 Created):**
```json
{
  "trackingId": "TRK1234567890",
  "barcode": "data:image/png;base64,iVBORw0KGgoAAAANS...",
  "currentStatus": "BOOKED",
  "message": "Parcel created successfully"
}
```

#### 4.3.2 Update Parcel Status

**Endpoint:** `PUT /api/v1/admin/parcels/{trackingId}/status`

**Description:** Update the status and location of a parcel

**Path Parameters:**
- `trackingId` (String, required): Tracking identifier

**Request Body:**
```json
{
  "status": "OUT_FOR_DELIVERY",
  "location": "Los Angeles Distribution Center",
  "notes": "Out for delivery - driver assigned"
}
```

**Success Response (200 OK):**
```json
{
  "trackingId": "TRK1234567890",
  "currentStatus": "OUT_FOR_DELIVERY",
  "currentLocation": "Los Angeles Distribution Center",
  "lastUpdateTimestamp": "2025-11-27T14:45:00Z",
  "message": "Status updated successfully"
}
```

#### 4.3.3 Get All Parcels

**Endpoint:** `GET /api/v1/admin/parcels`

**Description:** Retrieve all parcels with pagination

**Query Parameters:**
- `page` (Integer, optional, default: 0): Page number
- `size` (Integer, optional, default: 20): Page size
- `status` (String, optional): Filter by status

**Success Response (200 OK):**
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8
}
```

---

## 5. Data Model

### 5.1 Parcel Entity

```java
@Entity
@Table(name = "parcels")
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(unique = true, nullable = false, length = 15)
    private String trackingId;
    
    @Column(nullable = false)
    private String barcode;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParcelStatus currentStatus;
    
    @Column(nullable = false)
    private String currentLocation;
    
    @Column(nullable = false)
    private String originLocation;
    
    @Column(nullable = false)
    private String destinationLocation;
    
    @Column(nullable = false)
    private String recipientName;
    
    @Column(nullable = false)
    private String recipientAddress;
    
    @Column
    private LocalDate estimatedDeliveryDate;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;
    
    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StatusHistory> statusHistory;
}
```

### 5.2 StatusHistory Entity

```java
@Entity
@Table(name = "status_history")
public class StatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcel_id", nullable = false)
    private Parcel parcel;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParcelStatus status;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(length = 500)
    private String notes;
}
```

### 5.3 Enumerations

```java
public enum ParcelStatus {
    BOOKED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    DELAYED,
    CANCELLED,
    RETURNED
}
```

---

## 6. Business Logic Implementation

### 6.1 ParcelService Interface

```java
public interface ParcelService {
    ParcelTrackingResponse trackParcel(String trackingId);
    ParcelCreateResponse createParcel(ParcelCreateRequest request);
    ParcelTrackingResponse updateParcelStatus(String trackingId, ParcelStatusUpdateRequest request);
    Page<ParcelTrackingResponse> getAllParcels(Pageable pageable, ParcelStatus status);
}
```

### 6.2 Key Business Rules

1. **Tracking ID Generation**: Auto-generate unique 12-character alphanumeric tracking ID with prefix "TRK"
2. **Barcode Generation**: Generate Code 128 barcode upon parcel creation
3. **Status Validation**: Validate status transitions (e.g., cannot go from DELIVERED to IN_TRANSIT)
4. **Notification Triggers**: Send notifications on status changes to BOOKED, OUT_FOR_DELIVERY, and DELIVERED
5. **Audit Trail**: Maintain complete status history with timestamps
6. **Estimated Delivery**: Calculate based on origin/destination and current date

---

## 7. Security Requirements

### 7.1 Authentication & Authorization

- **Customer Endpoints**: Public access (read-only tracking)
- **Admin Endpoints**: Require authentication (OAuth2/JWT)
- **API Keys**: Optional rate limiting for customer endpoints

### 7.2 Input Validation

```java
@NotBlank(message = "Tracking ID is required")
@Size(min = 10, max = 15, message = "Tracking ID must be 10-15 characters")
@Pattern(regexp = "^[A-Z0-9]+$", message = "Tracking ID must be alphanumeric")
private String trackingId;
```

### 7.3 Security Headers

- CORS configuration for allowed origins
- CSRF protection enabled
- XSS protection headers
- No sensitive data in logs or error messages

---

## 8. Caching Strategy

### 8.1 Cache Configuration

```java
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new CaffeineCacheManager("parcels", "trackingData");
    }
    
    @Bean
    public CaffeineCache parcelsCache() {
        return Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();
    }
}
```

### 8.2 Cacheable Operations

- `@Cacheable("parcels")` on `trackParcel()` method
- `@CacheEvict("parcels")` on `updateParcelStatus()` method
- TTL: 10 minutes for tracking data
- Maximum cache size: 1000 entries

---

## 9. Configuration Management

### 9.1 application.properties (Base)

```properties
spring.application.name=parcel-tracking-service
server.port=8080
spring.jpa.open-in-view=false
management.endpoints.web.exposure.include=health,info,metrics
```

### 9.2 application-dev.properties

```properties
spring.datasource.url=jdbc:h2:mem:parceldb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
logging.level.com.hcl.parceltracking=DEBUG
management.endpoint.health.show-details=always
```

### 9.3 application-prod.properties

```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/parceldb
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
logging.level.com.hcl.parceltracking=INFO
management.endpoint.health.show-details=never
```

---

## 10. Error Handling

### 10.1 Custom Exceptions

- `ResourceNotFoundException`: When tracking ID not found (404)
- `InvalidTrackingIdException`: When tracking ID format is invalid (400)
- `InvalidStatusTransitionException`: When status change is not allowed (400)

### 10.2 Error Response Format

```json
{
  "timestamp": "2025-11-27T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Parcel with tracking ID 'TRK9999999999' not found",
  "path": "/api/v1/parcels/track/TRK9999999999"
}
```

### 10.3 Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex);
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex);
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex);
}
```

---

## 11. Non-Functional Requirements

### 11.1 Performance

- **Response Time**: < 3 seconds for tracking queries under normal load
- **Throughput**: Support 1000 requests/second
- **Database Connection Pool**: HikariCP with min 10, max 50 connections

### 11.2 Availability

- **Uptime**: 99.9% availability (SLA)
- **Health Checks**: Actuator `/actuator/health` endpoint
- **Graceful Shutdown**: 30-second grace period

### 11.3 Scalability

- **Horizontal Scaling**: Stateless design, can run multiple instances
- **Database**: Read replicas for query optimization
- **Caching**: Caffeine local cache per instance

### 11.4 Observability

- **Logging**: SLF4J with Logback, JSON format in production
- **Metrics**: Micrometer with Prometheus endpoint
- **Health Endpoint**: `/actuator/health` for liveness/readiness probes

---

## 12. Testing Strategy

### 12.1 Unit Tests

- Test service layer with mocked repositories
- Test controllers with `@WebMvcTest`
- Target: 80%+ code coverage

### 12.2 Integration Tests

- `@SpringBootTest` with H2 database
- Test full API flow from controller to database
- Test exception handling and validation

### 12.3 Test Data

```java
@TestConfiguration
public class TestDataConfig {
    @Bean
    public CommandLineRunner loadTestData(ParcelRepository repository) {
        return args -> {
            // Create sample parcels for testing
        };
    }
}
```

---

## 13. Deployment

### 13.1 Docker Configuration

**Dockerfile:**
```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 13.2 Environment Variables

```
DB_HOST=postgres-server
DB_PORT=5432
DB_USERNAME=parcel_user
DB_PASSWORD=<secret>
SPRING_PROFILES_ACTIVE=prod
```

### 13.3 Docker Compose (Development)

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
    depends_on:
      - postgres
      
  postgres:
    image: postgres:latest
    environment:
      POSTGRES_DB: parceldb
      POSTGRES_USER: parcel_user
      POSTGRES_PASSWORD: parcel_pass
    ports:
      - "5432:5432"
```

---

## 14. API Documentation

### 14.1 OpenAPI Configuration

```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI parcelTrackingOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Parcel Tracking System API")
                .description("REST API for tracking parcels and managing shipments")
                .version("1.0.0")
                .contact(new Contact()
                    .name("HCL Technologies")
                    .email("support@hcl.com")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Development"),
                new Server().url("https://api.parceltracking.com").description("Production")
            ));
    }
}
```

### 14.2 Swagger UI

- **URL**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

---

## 15. Monitoring & Alerting

### 15.1 Actuator Endpoints

- `/actuator/health`: Health status
- `/actuator/info`: Application information
- `/actuator/metrics`: Application metrics
- `/actuator/prometheus`: Prometheus metrics

### 15.2 Key Metrics

- Request rate and response time
- Database connection pool usage
- Cache hit/miss ratio
- Error rate by endpoint
- JVM memory and GC metrics

---

## 16. Development Workflow

### 16.1 Setup Instructions

```bash
# Clone repository
git clone <repository-url>
cd ParcelTrackingApp

# Build application
mvn clean install

# Run with dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Run tests
mvn test

# Build Docker image
docker build -t parcel-tracking-service:latest .

# Run with Docker Compose
docker-compose up
```

### 16.2 Code Quality

- **Checkstyle**: Enforce coding standards
- **SpotBugs**: Static analysis for bug detection
- **JaCoCo**: Code coverage reporting (minimum 80%)

---

## 17. Backstage Catalog Integration

### 17.1 catalog-info.yaml

```yaml
apiVersion: backstage.io/v1alpha1
kind: Component
metadata:
  name: parcel-tracking-service
  description: Microservice for real-time parcel tracking and shipment management
  tags:
    - java
    - spring-boot
    - microservice
    - logistics
  annotations:
    github.com/project-slug: alexstephen-github/ParcelTrackingApp
    sonarqube.org/project-key: parcel-tracking-service
  links:
    - url: http://localhost:8080/swagger-ui.html
      title: API Documentation
      icon: dashboard
    - url: http://localhost:8080/actuator/health
      title: Health Check
      icon: health
spec:
  type: service
  lifecycle: production
  owner: logistics-team
  system: parcel-management-system
  providesApis:
    - parcel-tracking-api
```

---

## 18. Future Enhancements

### 18.1 Phase 2 Features

- Real-time GPS tracking integration
- Map-based location visualization
- Predictive delivery time using ML
- Customer self-service portal with user registration
- Mobile app push notifications
- International shipment support with customs documentation

### 18.2 Technical Improvements

- Redis distributed caching for multi-instance deployments
- Kafka/RabbitMQ for asynchronous event processing
- GraphQL API as an alternative to REST
- Rate limiting with Redis
- Advanced security with OAuth2 and Spring Security

---

## 19. Compliance & Standards

- **GDPR**: Personal data handling and retention policies
- **SOC 2**: Security and availability controls
- **ISO 27001**: Information security management
- **REST API Best Practices**: RESTful design principles
- **OpenAPI 3.0 Specification**: API documentation standard

---

## 20. Appendices

### Appendix A: Database Schema

```sql
CREATE TABLE parcels (
    id UUID PRIMARY KEY,
    tracking_id VARCHAR(15) UNIQUE NOT NULL,
    barcode TEXT NOT NULL,
    current_status VARCHAR(50) NOT NULL,
    current_location VARCHAR(255) NOT NULL,
    origin_location VARCHAR(255) NOT NULL,
    destination_location VARCHAR(255) NOT NULL,
    recipient_name VARCHAR(255) NOT NULL,
    recipient_address TEXT NOT NULL,
    estimated_delivery_date DATE,
    created_at TIMESTAMP NOT NULL,
    last_updated_at TIMESTAMP NOT NULL
);

CREATE TABLE status_history (
    id UUID PRIMARY KEY,
    parcel_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    notes VARCHAR(500),
    FOREIGN KEY (parcel_id) REFERENCES parcels(id)
);

CREATE INDEX idx_tracking_id ON parcels(tracking_id);
CREATE INDEX idx_status ON parcels(current_status);
CREATE INDEX idx_parcel_history ON status_history(parcel_id);
```

### Appendix B: Maven Dependencies

```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-cache</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Caching -->
    <dependency>
        <groupId>com.github.ben-manes.caffeine</groupId>
        <artifactId>caffeine</artifactId>
    </dependency>
    
    <!-- OpenAPI Documentation -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.3.0</version>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

**Document Version History:**

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2025-11-27 | Development Team | Initial specification document |

---

**Approval Signatures:**

- Technical Lead: _________________________  Date: __________
- Project Manager: _______________________  Date: __________
- Security Officer: _______________________  Date: __________

---

*This document is confidential and proprietary to HCL Technologies. Unauthorized distribution is prohibited.*
