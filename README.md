# Parcel Tracking Service

Enterprise-grade microservice for real-time parcel tracking and shipment management built with Java 21 and Spring Boot 3.

## 🚀 Features

- **Real-time Parcel Tracking**: Track parcels using unique tracking IDs
- **Admin Management**: Create and update shipment status and locations
- **Barcode Generation**: Automatic Code 128 barcode generation for new parcels
- **Status History**: Complete audit trail of all status changes
- **Caching**: Caffeine-based caching for improved performance
- **API Documentation**: Interactive Swagger UI for API exploration
- **Health Monitoring**: Spring Boot Actuator for observability
- **Containerized**: Docker and Docker Compose support

## 🛠️ Technology Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.3+
- **Database**: PostgreSQL (Production), H2 (Development)
- **Caching**: Caffeine
- **API Documentation**: SpringDoc OpenAPI 3.0
- **Build Tool**: Maven 3.9+
- **Container**: Docker

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.9 or higher
- Docker and Docker Compose (optional)
- PostgreSQL 16 (for production)

## 🔧 Setup Instructions

### Local Development

1. **Clone the repository**
```powershell
git clone https://github.com/alexstephen-github/ParcelTrackingApp.git
cd ParcelTrackingApp
```

2. **Build the application**
```powershell
mvn clean install
```

3. **Run with development profile**
```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

4. **Access the application**
   - API Base URL: http://localhost:8080/api/v1
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console
   - Health Check: http://localhost:8080/actuator/health

### Using Docker Compose

1. **Build and start all services**
```powershell
docker-compose up --build
```

2. **Stop services**
```powershell
docker-compose down
```

3. **Clean up volumes**
```powershell
docker-compose down -v
```

## 📚 API Endpoints

### Customer Endpoints

- `GET /api/v1/parcels/track/{trackingId}` - Track a parcel by ID
- `GET /api/v1/parcels/health` - Health check

### Admin Endpoints

- `POST /api/v1/admin/parcels` - Create a new parcel
- `PUT /api/v1/admin/parcels/{trackingId}/status` - Update parcel status
- `GET /api/v1/admin/parcels` - Get all parcels (with pagination)
- `GET /api/v1/admin/parcels/{trackingId}` - Get parcel by ID
- `DELETE /api/v1/admin/parcels/{trackingId}` - Delete a parcel

## 🧪 Testing

### Run all tests
```powershell
mvn test
```

### Run with coverage
```powershell
mvn clean verify
```

### View coverage report
Open `target/site/jacoco/index.html` in a browser

## 📊 Monitoring & Observability

### Actuator Endpoints

- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics

## 🔐 Security

- Input validation using Jakarta Bean Validation
- Non-root Docker user for container security
- No sensitive data in logs or error messages
- Environment variable-based configuration for secrets

## 📝 Configuration

### Application Profiles

- **dev**: Development profile with H2 in-memory database
- **prod**: Production profile with PostgreSQL

### Environment Variables (Production)

- `DB_HOST`: PostgreSQL host (default: localhost)
- `DB_PORT`: PostgreSQL port (default: 5432)
- `DB_NAME`: Database name (default: parceldb)
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password

## 🏗️ Project Structure

```
src/main/java/com/hcl/parceltracking/
├── ParcelTrackingApplication.java
├── config/              # Configuration classes
├── controller/          # REST controllers
├── dto/                 # Data Transfer Objects
├── model/               # JPA Entities
├── repository/          # Spring Data repositories
├── service/             # Business logic
└── exception/           # Exception handling
```

## 📖 Documentation

- [Technical Specification](SPECIFICATION.md)
- [Functional Document](functional-document.txt)
- [API Guidelines](resta-api-spring-boot.md)

## 🤝 Contributing

1. Follow the coding standards defined in `resta-api-spring-boot.md`
2. Write unit tests for all new features
3. Maintain 80%+ code coverage
4. Update documentation as needed

## 📄 License

Copyright © 2025 HCL Technologies. All rights reserved.

## 👥 Contact

- **Team**: Logistics Team
- **Email**: support@hcl.com
- **Repository**: https://github.com/alexstephen-github/ParcelTrackingApp

## 🎯 Future Enhancements

- Real-time GPS tracking integration
- Map-based location visualization
- Customer self-service portal
- Mobile app push notifications
- International shipment support
- Redis distributed caching
- Kafka event streaming
