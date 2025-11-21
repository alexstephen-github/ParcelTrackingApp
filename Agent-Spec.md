# 🤖 Agent Protocol: Parcel Tracking System - Microservice Specification

======================================================================
**VERSION:** 1.0  
**DATE:** November 21, 2025  
**PROJECT:** Parcel Tracking System (PTS)  
**REPOSITORY:** ParcelTrackingApp  
**AUTHOR:** Generated from Functional Specification Document  
======================================================================

---

## 1. 🎯 Core Principles

-   **Security First**: Implement security controls at every layer (transport, application, data). Admin authentication is required using Spring Security with role-based access control. Customer tracking endpoints are public but rate-limited to prevent abuse.

-   **Production Ready**: All code must be production-ready with robust error handling, comprehensive logging, automated deployment pipelines, and performance monitoring via Spring Boot Actuator.

-   **Developer Experience**: Clear API documentation via OpenAPI/Swagger UI, intuitive project structure, comprehensive README with setup instructions, and fast local development with H2 in-memory database.

-   **Performance by Design**: Target 3-second response time for tracking queries. Implement caching for frequently accessed tracking data using Caffeine/Redis. Optimize database queries to prevent N+1 issues.

-   **Observability**: Comprehensive logging with SLF4J/Logback in JSON format, metrics collection via Micrometer, distributed tracing support, and health check endpoints for liveness/readiness probes.

-   **Resilience**: Implement retry logic for external service calls (e.g., Maps API), circuit breakers for third-party dependencies, and graceful degradation when optional features are unavailable.

---

## 2. 🛠️ Standard Technology Stack

| Component | Technology | Version/Standard | Justification |
|---|---|---|---|
| **Language** | Java | **21** | Latest LTS with modern language features (Records, Pattern Matching, Virtual Threads). |
| **Framework** | Spring Boot | **3.3+** | Industry standard for microservices, rapid development, extensive ecosystem. |
| **Build Tool** | Maven | 3.9+ | Robust dependency management, comprehensive plugin ecosystem. |
| **Database** | PostgreSQL (Prod), H2 (Dev/Test) | Latest | PostgreSQL: Production-grade, ACID compliant. H2: Fast in-memory for development. |
| **API Spec** | OpenAPI | 3.0 | Standardized API documentation via Springdoc OpenAPI. |
| **Container** | Docker | Latest | Consistent deployment environments. |
| **Caching** | Caffeine (local), Redis (distributed) | Latest | High-performance caching for tracking data. |
| **Validation** | Jakarta Bean Validation | 3.0 | Annotation-based validation for DTOs. |
| **Testing** | JUnit 5, Mockito, AssertJ, Testcontainers | Latest | Comprehensive testing framework. |
| **Monitoring** | Micrometer, Spring Boot Actuator | Latest | Production-ready metrics and health checks. |
| **Security** | Spring Security | 6.x | Secure admin endpoints with OAuth2/JWT. |
| **External APIs** | Third-party Maps API | Latest | For location display and mapping features. |

---

## 3. 🏗️ Project Initialization & Structure

### 3.1. Project Initialization

Generate the project using **Spring Initializr** (`start.spring.io`) with:
-   **Java Version**: 21
-   **Spring Boot Version**: 3.3+
-   **Dependencies**:
    -   Spring Web
    -   Spring Data JPA
    -   PostgreSQL Driver
    -   H2 Database
    -   Spring Boot Actuator
    -   Springdoc OpenAPI UI (`org.springdoc:springdoc-openapi-starter-webmvc-ui`)
    -   Spring Cache Abstraction
    -   Caffeine
    -   Jakarta Bean Validation
    -   Spring Security (for admin module)
    -   Lombok (optional)

### 3.2. Standard Package Structure

```
src/main/java/com/hcl/parceltracking/
├── ParcelTrackingApplication.java          # Main application class with @SpringBootApplication
├── config/                                  # Configuration classes
│   ├── SecurityConfig.java                  # Spring Security configuration (JWT/OAuth2)
│   ├── CacheConfig.java                     # Caffeine/Redis cache configuration
│   ├── OpenApiConfig.java                   # Swagger/OpenAPI configuration
│   └── WebConfig.java                       # CORS, interceptors, etc.
├── controller/                              # REST controllers (API layer)
│   ├── CustomerTrackingController.java      # Customer tracking endpoints
│   ├── AdminShipmentController.java         # Admin shipment management endpoints
│   └── validation/                          # Custom validation annotations
├── dto/                                     # Data Transfer Objects
│   ├── request/                             # Request DTOs
│   │   ├── TrackingRequest.java             # { trackingId: "1234567890" }
│   │   ├── UpdateShipmentRequest.java       # { trackingId, status, location }
│   │   └── NotificationRequest.java         # { trackingId, email, sms }
│   └── response/                            # Response DTOs
│       ├── TrackingResponse.java            # { trackingId, status, location, lastUpdate, estimatedDelivery }
│       ├── ErrorResponse.java               # Standardized error response
│       └── ShipmentUpdateResponse.java      # Confirmation response for admin updates
├── model/                                   # JPA Entities
│   ├── Shipment.java                        # Main shipment entity
│   ├── ShipmentStatus.java                  # Enum: BOOKED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, DELAYED
│   ├── Location.java                        # Location entity with coordinates
│   └── StatusHistory.java                   # Audit trail for status changes
├── repository/                              # Spring Data JPA repositories
│   ├── ShipmentRepository.java              # findByTrackingId, findByStatus, etc.
│   ├── LocationRepository.java              # Location data access
│   └── StatusHistoryRepository.java         # Status change history
├── service/                                 # Business logic layer
│   ├── CustomerTrackingService.java         # Track parcel logic
│   ├── AdminShipmentService.java            # Update shipment, generate barcode
│   ├── NotificationService.java             # Email/SMS notifications
│   ├── BarcodeGenerationService.java        # Generate unique barcodes
│   └── impl/                                # Service implementations
│       ├── CustomerTrackingServiceImpl.java
│       ├── AdminShipmentServiceImpl.java
│       ├── NotificationServiceImpl.java
│       └── BarcodeGenerationServiceImpl.java
├── mapper/                                  # DTO-Entity mappers
│   └── ShipmentMapper.java                  # MapStruct or manual mapping
├── util/                                    # Utility classes
│   ├── BarcodeGenerator.java                # Barcode generation utility
│   └── Constants.java                       # Application constants
└── exception/                               # Custom exceptions and global handler
    ├── ResourceNotFoundException.java       # Thrown when tracking ID is invalid
    ├── InvalidTrackingIdException.java      # Validation error for tracking ID format
    ├── UnauthorizedException.java           # Admin authentication failures
    └── GlobalExceptionHandler.java          # @RestControllerAdvice for centralized error handling

src/test/java/com/hcl/parceltracking/
├── controller/                              # Controller integration tests
├── service/                                 # Service unit tests
└── repository/                              # Repository integration tests with Testcontainers

src/main/resources/
├── application.properties                   # Base configuration
├── application-dev.properties               # Development (H2, debug logging)
├── application-prod.properties              # Production (PostgreSQL, optimized logging)
├── application-test.properties              # Test configuration
├── logback-spring.xml                       # Structured logging configuration
└── db/migration/                            # Flyway/Liquibase migration scripts
    ├── V1__create_shipment_table.sql
    ├── V2__create_location_table.sql
    └── V3__create_status_history_table.sql
```

---

## 4. 📝 Functional Requirements Implementation

### 4.1. Customer Module (FR-C1, FR-C2)

#### FR-C1: Track Parcel Status

**Endpoint**: `GET /api/v1/tracking/{trackingId}`

**Controller**: `CustomerTrackingController`
```java
@RestController
@RequestMapping("/api/v1/tracking")
@Tag(name = "Customer Tracking", description = "APIs for customers to track their parcels")
public class CustomerTrackingController {
    
    private final CustomerTrackingService trackingService;
    
    @GetMapping("/{trackingId}")
    @Operation(summary = "Track parcel by tracking ID", description = "Returns current status, location, and estimated delivery")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tracking information retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Invalid tracking ID"),
        @ApiResponse(responseCode = "429", description = "Too many requests - rate limit exceeded")
    })
    public ResponseEntity<TrackingResponse> trackParcel(
            @PathVariable @Pattern(regexp = "^[A-Z0-9]{10,15}$", message = "Invalid tracking ID format") 
            String trackingId) {
        
        TrackingResponse response = trackingService.getTrackingInfo(trackingId);
        return ResponseEntity.ok(response);
    }
}
```

**Service**: `CustomerTrackingService`
```java
@Service
@Transactional(readOnly = true)
public class CustomerTrackingServiceImpl implements CustomerTrackingService {
    
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper mapper;
    
    @Cacheable(value = "trackingCache", key = "#trackingId")
    public TrackingResponse getTrackingInfo(String trackingId) {
        Shipment shipment = shipmentRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException("Tracking ID not found: " + trackingId));
        
        return mapper.toTrackingResponse(shipment);
    }
}
```

**Response DTO**: `TrackingResponse`
```java
public record TrackingResponse(
    String trackingId,
    String currentLocation,
    ShipmentStatus status,
    LocalDateTime lastUpdate,
    LocalDate estimatedDelivery,
    List<StatusHistoryDto> history
) {}
```

#### FR-C2: Receive Notifications

**Service**: `NotificationService`
```java
@Service
public class NotificationServiceImpl implements NotificationService {
    
    private final JavaMailSender mailSender;
    // SMS provider client (e.g., Twilio)
    
    @Async
    public void sendStatusUpdateNotification(Shipment shipment, ShipmentStatus newStatus) {
        // Send email notification
        sendEmailNotification(shipment.getCustomerEmail(), shipment.getTrackingId(), newStatus);
        
        // Send SMS notification if configured
        if (shipment.getCustomerPhone() != null) {
            sendSmsNotification(shipment.getCustomerPhone(), shipment.getTrackingId(), newStatus);
        }
    }
    
    private void sendEmailNotification(String email, String trackingId, ShipmentStatus status) {
        // Email implementation
    }
    
    private void sendSmsNotification(String phone, String trackingId, ShipmentStatus status) {
        // SMS implementation
    }
}
```

### 4.2. Admin Module (FR-A1, FR-A2)

#### FR-A1: Update Shipment Status

**Endpoint**: `PUT /api/v1/admin/shipments/{trackingId}/status`

**Controller**: `AdminShipmentController`
```java
@RestController
@RequestMapping("/api/v1/admin/shipments")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Shipment Management", description = "APIs for administrators to manage shipments")
public class AdminShipmentController {
    
    private final AdminShipmentService shipmentService;
    
    @PutMapping("/{trackingId}/status")
    @Operation(summary = "Update shipment status", description = "Update status and location of a shipment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Shipment updated successfully"),
        @ApiResponse(responseCode = "404", description = "Shipment not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    public ResponseEntity<ShipmentUpdateResponse> updateShipmentStatus(
            @PathVariable String trackingId,
            @Valid @RequestBody UpdateShipmentRequest request) {
        
        ShipmentUpdateResponse response = shipmentService.updateShipment(trackingId, request);
        return ResponseEntity.ok(response);
    }
}
```

**Request DTO**: `UpdateShipmentRequest`
```java
public record UpdateShipmentRequest(
    @NotNull(message = "Status is required")
    ShipmentStatus status,
    
    @NotBlank(message = "Location is required")
    @Size(max = 255)
    String location,
    
    String notes
) {}
```

**Service**: `AdminShipmentService`
```java
@Service
@Transactional
public class AdminShipmentServiceImpl implements AdminShipmentService {
    
    private final ShipmentRepository shipmentRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final NotificationService notificationService;
    
    @CacheEvict(value = "trackingCache", key = "#trackingId")
    public ShipmentUpdateResponse updateShipment(String trackingId, UpdateShipmentRequest request) {
        Shipment shipment = shipmentRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found: " + trackingId));
        
        // Update shipment
        ShipmentStatus oldStatus = shipment.getStatus();
        shipment.setStatus(request.status());
        shipment.setCurrentLocation(request.location());
        shipment.setLastUpdate(LocalDateTime.now());
        
        // Save status history
        StatusHistory history = new StatusHistory(shipment, oldStatus, request.status(), request.location());
        statusHistoryRepository.save(history);
        
        shipmentRepository.save(shipment);
        
        // Trigger notification if significant status change
        if (isSignificantStatusChange(oldStatus, request.status())) {
            notificationService.sendStatusUpdateNotification(shipment, request.status());
        }
        
        return new ShipmentUpdateResponse(trackingId, "Shipment updated successfully", LocalDateTime.now());
    }
    
    private boolean isSignificantStatusChange(ShipmentStatus oldStatus, ShipmentStatus newStatus) {
        return newStatus == ShipmentStatus.OUT_FOR_DELIVERY || 
               newStatus == ShipmentStatus.DELIVERED ||
               newStatus == ShipmentStatus.DELAYED;
    }
}
```

#### FR-A2: Generate Barcode

**Endpoint**: `POST /api/v1/admin/shipments`

**Controller Method**:
```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
@Operation(summary = "Create new shipment", description = "Creates a new shipment with auto-generated barcode")
public ResponseEntity<ShipmentCreateResponse> createShipment(@Valid @RequestBody CreateShipmentRequest request) {
    ShipmentCreateResponse response = shipmentService.createShipment(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

**Service Method**:
```java
@Transactional
public ShipmentCreateResponse createShipment(CreateShipmentRequest request) {
    // Generate unique tracking ID (barcode)
    String trackingId = barcodeGenerationService.generateUniqueTrackingId();
    
    Shipment shipment = new Shipment();
    shipment.setTrackingId(trackingId);
    shipment.setStatus(ShipmentStatus.BOOKED);
    shipment.setCustomerEmail(request.customerEmail());
    shipment.setCustomerPhone(request.customerPhone());
    shipment.setOrigin(request.origin());
    shipment.setDestination(request.destination());
    shipment.setCreatedAt(LocalDateTime.now());
    
    shipmentRepository.save(shipment);
    
    return new ShipmentCreateResponse(trackingId, "Shipment created successfully", shipment.getCreatedAt());
}
```

**Barcode Generation Service**:
```java
@Service
public class BarcodeGenerationServiceImpl implements BarcodeGenerationService {
    
    private final ShipmentRepository shipmentRepository;
    private final SecureRandom random = new SecureRandom();
    
    public String generateUniqueTrackingId() {
        String trackingId;
        do {
            trackingId = generateRandomAlphanumeric(12); // 12-character alphanumeric
        } while (shipmentRepository.existsByTrackingId(trackingId));
        
        return trackingId;
    }
    
    private String generateRandomAlphanumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
```

---

## 5. 🗄️ Database Schema

### 5.1. Entity Definitions

**Shipment Entity**:
```java
@Entity
@Table(name = "shipments", indexes = {
    @Index(name = "idx_tracking_id", columnList = "tracking_id", unique = true),
    @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@ToString(exclude = {"statusHistory"})
@EqualsAndHashCode(exclude = {"id", "statusHistory"})
public class Shipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tracking_id", unique = true, nullable = false, length = 15)
    private String trackingId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ShipmentStatus status;
    
    @Column(name = "current_location", length = 255)
    private String currentLocation;
    
    @Column(name = "origin", nullable = false, length = 255)
    private String origin;
    
    @Column(name = "destination", nullable = false, length = 255)
    private String destination;
    
    @Column(name = "customer_email", length = 100)
    private String customerEmail;
    
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;
    
    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;
    
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StatusHistory> statusHistory = new ArrayList<>();
}
```

**ShipmentStatus Enum**:
```java
public enum ShipmentStatus {
    BOOKED("Shipment booked"),
    IN_TRANSIT("In transit"),
    OUT_FOR_DELIVERY("Out for delivery"),
    DELIVERED("Delivered"),
    DELAYED("Delayed");
    
    private final String description;
    
    ShipmentStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
```

**StatusHistory Entity**:
```java
@Entity
@Table(name = "status_history")
@Getter
@Setter
public class StatusHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", length = 50)
    private ShipmentStatus oldStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 50)
    private ShipmentStatus newStatus;
    
    @Column(name = "location", length = 255)
    private String location;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    public StatusHistory() {
        this.timestamp = LocalDateTime.now();
    }
    
    public StatusHistory(Shipment shipment, ShipmentStatus oldStatus, ShipmentStatus newStatus, String location) {
        this.shipment = shipment;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.location = location;
        this.timestamp = LocalDateTime.now();
    }
}
```

### 5.2. Flyway Migration Scripts

**V1__create_shipment_table.sql**:
```sql
CREATE TABLE shipments (
    id BIGSERIAL PRIMARY KEY,
    tracking_id VARCHAR(15) UNIQUE NOT NULL,
    status VARCHAR(50) NOT NULL,
    current_location VARCHAR(255),
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    customer_email VARCHAR(100),
    customer_phone VARCHAR(20),
    estimated_delivery DATE,
    last_update TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tracking_id ON shipments(tracking_id);
CREATE INDEX idx_status ON shipments(status);
```

**V2__create_status_history_table.sql**:
```sql
CREATE TABLE status_history (
    id BIGSERIAL PRIMARY KEY,
    shipment_id BIGINT NOT NULL,
    old_status VARCHAR(50),
    new_status VARCHAR(50) NOT NULL,
    location VARCHAR(255),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shipment_id) REFERENCES shipments(id) ON DELETE CASCADE
);

CREATE INDEX idx_shipment_id ON status_history(shipment_id);
CREATE INDEX idx_timestamp ON status_history(timestamp);
```

---

## 6. 🔒 Security Configuration

### 6.1. Spring Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable for REST API (use JWT)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/tracking/**").permitAll() // Public tracking endpoints
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // Admin endpoints
                .requestMatchers("/actuator/health/**", "/actuator/info").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder()))
            );
        
        return http.build();
    }
    
    @Bean
    public JwtDecoder jwtDecoder() {
        // Configure JWT decoder (e.g., using JWK Set URI)
        return NimbusJwtDecoder.withJwkSetUri("https://your-identity-provider/.well-known/jwks.json").build();
    }
}
```

### 6.2. Rate Limiting for Customer Endpoints

```java
@Configuration
public class RateLimitConfig {
    
    @Bean
    public RateLimiter rateLimiter() {
        // Configure rate limiter (e.g., using Bucket4j or Resilience4j)
        // Limit: 100 requests per minute per IP for tracking endpoints
        return RateLimiter.of("trackingRateLimiter", RateLimiterConfig.custom()
                .limitForPeriod(100)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofSeconds(5))
                .build());
    }
}
```

---

## 7. 🗂️ Exception Handling

### 7.1. Global Exception Handler

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Input validation failed",
                request.getDescription(false).replace("uri=", ""),
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred", ex);
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred. Please try again later.",
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

**ErrorResponse DTO**:
```java
public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<String> details
) {
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this(timestamp, status, error, message, path, Collections.emptyList());
    }
}
```

---

## 8. 💾 Caching Strategy

### 8.1. Cache Configuration

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("trackingCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES) // Cache for 5 minutes
                .maximumSize(1000) // Max 1000 entries
                .recordStats());
        return cacheManager;
    }
}
```

### 8.2. Cache Usage

-   **Cacheable**: Applied to `getTrackingInfo()` method in `CustomerTrackingService`
-   **CacheEvict**: Applied to `updateShipment()` method in `AdminShipmentService` to invalidate cache when status is updated

---

## 9. 📊 Observability & Monitoring

### 9.1. Application Properties

```properties
# Actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=when-authorized
management.health.livenessState.enabled=true
management.health.readinessState.enabled=true

# Metrics
management.metrics.export.prometheus.enabled=true
management.metrics.tags.application=${spring.application.name}
```

### 9.2. Health Indicators

```java
@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    
    private final ShipmentRepository shipmentRepository;
    
    @Override
    public Health health() {
        try {
            long count = shipmentRepository.count();
            return Health.up()
                    .withDetail("database", "PostgreSQL")
                    .withDetail("shipment_count", count)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
```

### 9.3. Structured Logging

**logback-spring.xml**:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <includeContext>true</includeContext>
            <includeMdc>true</includeMdc>
            <includeStructuredArguments>true</includeStructuredArguments>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
    
    <logger name="com.hcl.parceltracking" level="DEBUG" additivity="false">
        <appender-ref ref="CONSOLE"/>
    </logger>
</configuration>
```

---

## 10. 🐳 Containerization

### 10.1. Dockerfile

```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --retries=5 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health/liveness || exit 1

# Run application
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
```

### 10.2. docker-compose.yml (Development)

```yaml
version: '3.8'

services:
  parcel-tracking-app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/actuator/health/liveness"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s

  postgres:
    image: postgres:16-alpine
    environment:
      - POSTGRES_DB=parceltracking
      - POSTGRES_USER=admin
      - POSTGRES_PASSWORD=admin123
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

---

## 11. 📚 API Documentation

### 11.1. OpenAPI Configuration

```java
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI parcelTrackingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Parcel Tracking System API")
                        .description("REST API for tracking parcels and managing shipments")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Parcel Tracking Team")
                                .email("support@hcl.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development Server"),
                        new Server().url("https://api.parceltracking.hcl.com").description("Production Server")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Authorization header using Bearer scheme")));
    }
}
```

### 11.2. API Endpoints Summary

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/tracking/{trackingId}` | Get tracking information | No |
| POST | `/api/v1/admin/shipments` | Create new shipment | Yes (ADMIN) |
| PUT | `/api/v1/admin/shipments/{trackingId}/status` | Update shipment status | Yes (ADMIN) |
| GET | `/api/v1/admin/shipments` | List all shipments | Yes (ADMIN) |
| GET | `/api/v1/admin/shipments/{trackingId}` | Get shipment details | Yes (ADMIN) |
| GET | `/actuator/health` | Health check endpoint | No |
| GET | `/swagger-ui.html` | Swagger UI | No |

---

## 12. 🧪 Testing Strategy

### 12.1. Unit Tests

```java
@ExtendWith(MockitoExtension.class)
class CustomerTrackingServiceImplTest {
    
    @Mock
    private ShipmentRepository shipmentRepository;
    
    @Mock
    private ShipmentMapper mapper;
    
    @InjectMocks
    private CustomerTrackingServiceImpl trackingService;
    
    @Test
    void getTrackingInfo_ValidTrackingId_ReturnsTrackingResponse() {
        // Given
        String trackingId = "ABC123456789";
        Shipment shipment = new Shipment();
        shipment.setTrackingId(trackingId);
        shipment.setStatus(ShipmentStatus.IN_TRANSIT);
        
        TrackingResponse expectedResponse = new TrackingResponse(
                trackingId, "New York", ShipmentStatus.IN_TRANSIT, 
                LocalDateTime.now(), LocalDate.now().plusDays(2), Collections.emptyList()
        );
        
        when(shipmentRepository.findByTrackingId(trackingId)).thenReturn(Optional.of(shipment));
        when(mapper.toTrackingResponse(shipment)).thenReturn(expectedResponse);
        
        // When
        TrackingResponse result = trackingService.getTrackingInfo(trackingId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.trackingId()).isEqualTo(trackingId);
        assertThat(result.status()).isEqualTo(ShipmentStatus.IN_TRANSIT);
        
        verify(shipmentRepository).findByTrackingId(trackingId);
        verify(mapper).toTrackingResponse(shipment);
    }
    
    @Test
    void getTrackingInfo_InvalidTrackingId_ThrowsResourceNotFoundException() {
        // Given
        String trackingId = "INVALID123";
        when(shipmentRepository.findByTrackingId(trackingId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> trackingService.getTrackingInfo(trackingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Tracking ID not found");
        
        verify(shipmentRepository).findByTrackingId(trackingId);
    }
}
```

### 12.2. Integration Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class CustomerTrackingControllerIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ShipmentRepository shipmentRepository;
    
    @BeforeEach
    void setUp() {
        shipmentRepository.deleteAll();
    }
    
    @Test
    void trackParcel_ValidTrackingId_ReturnsOk() throws Exception {
        // Given
        Shipment shipment = new Shipment();
        shipment.setTrackingId("TEST12345678");
        shipment.setStatus(ShipmentStatus.IN_TRANSIT);
        shipment.setCurrentLocation("New York");
        shipment.setOrigin("Boston");
        shipment.setDestination("Los Angeles");
        shipment.setCreatedAt(LocalDateTime.now());
        shipmentRepository.save(shipment);
        
        // When & Then
        mockMvc.perform(get("/api/v1/tracking/TEST12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingId").value("TEST12345678"))
                .andExpect(jsonPath("$.status").value("IN_TRANSIT"))
                .andExpect(jsonPath("$.currentLocation").value("New York"));
    }
    
    @Test
    void trackParcel_InvalidTrackingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tracking/INVALID123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Tracking ID not found")));
    }
}
```

---

## 13. 🚀 Deployment & CI/CD

### 13.1. Environment Variables (Production)

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-host:5432/parceltracking
SPRING_DATASOURCE_USERNAME=${DB_USERNAME}
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}

# Security
JWT_SECRET=${JWT_SECRET}
JWT_ISSUER=https://auth.hcl.com

# External Services
MAPS_API_KEY=${MAPS_API_KEY}
SMTP_HOST=${SMTP_HOST}
SMTP_PORT=587
SMTP_USERNAME=${SMTP_USERNAME}
SMTP_PASSWORD=${SMTP_PASSWORD}

# Monitoring
MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,metrics,prometheus
```

### 13.2. Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: parcel-tracking-service
  labels:
    app: parcel-tracking
spec:
  replicas: 3
  selector:
    matchLabels:
      app: parcel-tracking
  template:
    metadata:
      labels:
        app: parcel-tracking
    spec:
      containers:
      - name: parcel-tracking
        image: hcl/parcel-tracking:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: SPRING_DATASOURCE_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
        - name: SPRING_DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: username
        - name: SPRING_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: password
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 5
---
apiVersion: v1
kind: Service
metadata:
  name: parcel-tracking-service
spec:
  selector:
    app: parcel-tracking
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer
```

---

## 14. 📋 catalog-info.yaml

```yaml
apiVersion: backstage.io/v1alpha1
kind: Component
metadata:
  name: parcel-tracking-service
  description: Microservice for tracking parcels with real-time status updates and admin management
  tags:
    - java
    - spring-boot
    - microservice
    - rest-api
    - logistics
  annotations:
    github.com/project-slug: alexstephen-github/ParcelTrackingApp
    jira.com/project-key: PTS
    jenkins.io/job-full-name: logistics/parcel-tracking-ci
    sonarqube.org/project-key: com.hcl.parceltracking
    grafana.com/dashboard-selector: "app=parcel-tracking-service"
  links:
    - url: https://confluence.hcl.com/display/LOGISTICS/Parcel+Tracking+Docs
      title: Service Documentation
      icon: dashboard
    - url: https://jenkins.hcl.com/job/parcel-tracking-cd
      title: CD Pipeline
      icon: cicd
    - url: https://grafana.hcl.com/d/parcel-tracking
      title: Operational Dashboard
      icon: monitor
spec:
  type: service
  lifecycle: production
  owner: team-logistics
  system: shipping-logistics
  providesApis:
    - parcel-tracking-api
  consumesApis:
    - maps-api
    - notification-service-api
  dependsOn:
    - resource:database/parcel-tracking-db
    - component:redis-cache-cluster
```

---

## 15. 📖 README.md Structure

```markdown
# Parcel Tracking System

REST API for tracking parcels with real-time status updates and administrative shipment management.

## Features

- ✅ Real-time parcel tracking by tracking ID
- ✅ Automated notifications (Email/SMS) for significant status changes
- ✅ Admin portal for shipment management
- ✅ Automatic barcode generation for new shipments
- ✅ Comprehensive audit trail for status changes
- ✅ Rate-limited public API
- ✅ Secure admin endpoints with JWT authentication

## Tech Stack

- Java 21
- Spring Boot 3.3+
- PostgreSQL (Production) / H2 (Development)
- Spring Security (OAuth2/JWT)
- Caffeine Cache
- Docker & Docker Compose
- Flyway (Database Migrations)
- JUnit 5, Mockito, Testcontainers

## Quick Start

### Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose (for PostgreSQL)

### Running Locally

1. Clone the repository
```bash
git clone https://github.com/alexstephen-github/ParcelTrackingApp.git
cd ParcelTrackingApp
```

2. Start PostgreSQL (or use H2 for development)
```bash
docker-compose up -d postgres
```

3. Run the application
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

4. Access Swagger UI
```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

### Customer Tracking
- `GET /api/v1/tracking/{trackingId}` - Track parcel status

### Admin Management
- `POST /api/v1/admin/shipments` - Create new shipment
- `PUT /api/v1/admin/shipments/{trackingId}/status` - Update shipment status
- `GET /api/v1/admin/shipments` - List all shipments

### Health & Monitoring
- `GET /actuator/health` - Health check
- `GET /actuator/metrics` - Application metrics

## Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## Docker

```bash
# Build image
docker build -t parcel-tracking:latest .

# Run container
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod parcel-tracking:latest
```

## Configuration

Key environment variables:
- `SPRING_DATASOURCE_URL` - Database connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `JWT_SECRET` - JWT signing secret
- `MAPS_API_KEY` - Third-party maps API key

## License

Apache 2.0
```

---

## 16. ✅ Non-Functional Requirements Implementation

### 16.1. Performance (3-second load time)

- **Database Indexing**: Indexes on `tracking_id` and `status` columns
- **Caching**: 5-minute cache TTL for tracking queries using Caffeine
- **Query Optimization**: Use `@EntityGraph` to prevent N+1 queries
- **Connection Pooling**: HikariCP with optimized pool settings

```properties
# HikariCP Configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### 16.2. Security

- **Admin Authentication**: JWT-based authentication with Spring Security
- **Role-Based Access Control**: `@PreAuthorize("hasRole('ADMIN')")` on admin endpoints
- **Rate Limiting**: 100 requests/minute per IP for customer endpoints
- **SQL Injection Prevention**: JPA parameterized queries
- **Secrets Management**: All sensitive data via environment variables

### 16.3. Availability (99.9% uptime)

- **Health Checks**: Liveness and readiness probes for Kubernetes
- **Graceful Shutdown**: Spring Boot graceful shutdown enabled
- **Circuit Breakers**: Resilience4j for external API calls
- **Database Connection Pooling**: Automatic connection recovery

```properties
# Graceful shutdown
spring.lifecycle.timeout-per-shutdown-phase=30s
server.shutdown=graceful
```

### 16.4. Usability

- **Clear API Documentation**: OpenAPI/Swagger UI with detailed descriptions
- **Consistent Error Messages**: Standardized `ErrorResponse` DTO
- **Validation Messages**: User-friendly field-level validation errors
- **Simple Tracking Interface**: Single endpoint with tracking ID

---

## 17. 🎯 Coding Standards Checklist

### Java Spring Boot Standards

✅ Use `final` for immutable variables  
✅ Java Records for DTOs (immutable data carriers)  
✅ Constructor injection for all dependencies  
✅ `@Transactional` on service methods (with `readOnly = true` for queries)  
✅ Global exception handling with `@RestControllerAdvice`  
✅ Jakarta Bean Validation on all request DTOs  
✅ Proper separation of concerns (Controller → Service → Repository)  
✅ No direct entity exposure in API responses  
✅ Comprehensive JavaDoc for public APIs  
✅ Structured logging with SLF4J and Logback  
✅ Externalized configuration with environment variables  
✅ REST principles: resource-based URLs, proper HTTP methods/status codes  
✅ Prevent N+1 queries with `@EntityGraph` or `JOIN FETCH`  
✅ Unit tests with 80%+ coverage  
✅ Integration tests with Testcontainers  

---

## 18. 📝 Development Workflow Summary

1. **API Design**: Define DTOs (request/response) with validation annotations
2. **Controller Layer**: Create REST endpoints with proper HTTP mappings
3. **Service Layer**: Implement business logic with transactions and caching
4. **Repository Layer**: Define JPA repositories with custom queries
5. **Exception Handling**: Add custom exceptions and handlers
6. **Security**: Configure Spring Security for admin endpoints
7. **Testing**: Write unit and integration tests
8. **Documentation**: Add OpenAPI annotations and JavaDoc
9. **Containerization**: Create Dockerfile and docker-compose.yml
10. **Deployment**: Configure Kubernetes manifests and CI/CD pipeline

---

## 19. 🔗 References & Links

- [Functional Specification Document](./functional-document.txt)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://spring.io/projects/spring-security)
- [Springdoc OpenAPI](https://springdoc.org/)
- [Testcontainers](https://testcontainers.com/)
- [JPA Best Practices](https://vladmihalcea.com/tutorials/hibernate/)

---

**End of Agent Specification Document**
