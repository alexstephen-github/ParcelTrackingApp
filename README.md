# Parcel Tracking System

REST API for tracking parcels with real-time status updates and administrative shipment management.

## 🚀 Features

- ✅ Real-time parcel tracking by tracking ID
- ✅ Automated notifications (Email/SMS) for significant status changes
- ✅ Admin portal for shipment management
- ✅ Automatic barcode generation for new shipments
- ✅ Comprehensive audit trail for status changes
- ✅ Rate-limited public API
- ✅ Secure admin endpoints with JWT authentication

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.3+**
- **PostgreSQL** (Production) / **H2** (Development)
- **Spring Security** (OAuth2/JWT)
- **Caffeine Cache**
- **Docker & Docker Compose**
- **Flyway** (Database Migrations)
- **JUnit 5, Mockito, Testcontainers** (Testing)
- **OpenAPI/Swagger** (API Documentation)

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.9+
- Docker & Docker Compose (optional, for PostgreSQL)

## 🏃 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/alexstephen-github/ParcelTrackingApp.git
cd ParcelTrackingApp
```

### 2. Run with H2 (Development Mode)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Access the application:
- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

### 3. Run with PostgreSQL (Production Mode)

Start PostgreSQL using Docker Compose:

```bash
docker-compose up -d postgres
```

Run the application:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 4. Run with Docker

Build and run everything with Docker Compose:

```bash
docker-compose up --build
```

## 📚 API Endpoints

### Customer Tracking (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/tracking/{trackingId}` | Track parcel status |

**Example:**
```bash
curl http://localhost:8080/api/v1/tracking/ABC123456789
```

### Admin Management (Secured)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/admin/shipments` | Create new shipment |
| PUT | `/api/v1/admin/shipments/{trackingId}/status` | Update shipment status |

**Example:**
```bash
# Create shipment
curl -X POST http://localhost:8080/api/v1/admin/shipments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "origin": "Boston",
    "destination": "Los Angeles",
    "customerEmail": "customer@example.com",
    "estimatedDelivery": "2025-12-01"
  }'

# Update shipment status
curl -X PUT http://localhost:8080/api/v1/admin/shipments/ABC123456789/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "status": "OUT_FOR_DELIVERY",
    "location": "Los Angeles Distribution Center"
  }'
```

### Health & Monitoring

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/actuator/health` | Health check |
| GET | `/actuator/metrics` | Application metrics |
| GET | `/actuator/prometheus` | Prometheus metrics |

## 🧪 Testing

Run all tests:

```bash
mvn test
```

Run tests with coverage:

```bash
mvn test jacoco:report
```

View coverage report at: `target/site/jacoco/index.html`

## 🐳 Docker

### Build Docker Image

```bash
docker build -t parcel-tracking:latest .
```

### Run Container

```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/parceltracking \
  -e SPRING_DATASOURCE_USERNAME=admin \
  -e SPRING_DATASOURCE_PASSWORD=admin123 \
  parcel-tracking:latest
```

## ⚙️ Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | Database connection URL | `jdbc:postgresql://localhost:5432/parceltracking` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `admin` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `admin123` |
| `SMTP_HOST` | SMTP server host | `smtp.gmail.com` |
| `SMTP_PORT` | SMTP server port | `587` |
| `SMTP_USERNAME` | SMTP username | - |
| `SMTP_PASSWORD` | SMTP password | - |
| `JWT_SECRET` | JWT signing secret | - |

### Profiles

- **dev**: Development mode with H2 in-memory database
- **prod**: Production mode with PostgreSQL
- **test**: Test mode for integration tests

## 📖 Project Structure

```
src/
├── main/
│   ├── java/com/hcl/parceltracking/
│   │   ├── config/              # Configuration classes
│   │   ├── controller/          # REST controllers
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── exception/           # Custom exceptions
│   │   ├── mapper/              # DTO-Entity mappers
│   │   ├── model/               # JPA entities
│   │   ├── repository/          # Spring Data repositories
│   │   └── service/             # Business logic
│   └── resources/
│       ├── db/migration/        # Flyway SQL scripts
│       └── application*.properties
└── test/                        # Unit and integration tests
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

Apache 2.0

## 👥 Contact

Parcel Tracking Team - support@hcl.com

Project Link: https://github.com/alexstephen-github/ParcelTrackingApp
