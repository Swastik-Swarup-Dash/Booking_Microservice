# BookMyShow-Style Booking Microservice

A Spring Boot microservice implementing polyglot persistence architecture with PostgreSQL, MongoDB, and Redis.

## 🏗️ Architecture

This microservice follows the following database strategy:
- **PostgreSQL** - Transactional data (bookings, users, payments)
- **MongoDB** - Catalog data (movies, theaters, flexible schemas)
- **Redis** - Caching and real-time seat availability

## 🚀 Features Implemented

### ✅ Database Setup
- **PostgreSQL 15** - Primary relational database
- **MongoDB Community** - Document store for movie catalog
- **Redis** - In-memory cache for performance

### ✅ Movie Catalog API
- `GET /api/movies` - Get all movies
- `POST /api/movies` - Add new movie
- `GET /api/movies/genre/{genre}` - Filter by genre
- `GET /api/movies/search?title={title}` - Search movies

### ✅ Data Models
- **JPA Entities** - User, Booking, Theatre, Screen, Show, Ticket
- **MongoDB Document** - MovieCatalog with flexible schema
- **Redis Caching** - Automated caching with Spring Cache

### ✅ Services & Configuration
- **MovieService** - Business logic with caching
- **CacheService** - Redis operations for seat availability
- **SecurityConfig** - Spring Security setup
- **DatabaseConfig** - Multi-database configuration

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA** - PostgreSQL integration
- **Spring Data MongoDB** - MongoDB integration
- **Spring Data Redis** - Redis caching
- **Spring Security** - Authentication & authorization
- **Lombok** - Boilerplate reduction
- **Maven** - Dependency management

## 📦 Dependencies

```xml
<!-- Core Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Databases -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- Database Drivers -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
</dependency>
```

## 🔧 Setup & Installation

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 15
- MongoDB Community
- Redis

### Database Installation (macOS)
```bash
# PostgreSQL
brew install postgresql@15
brew services start postgresql@15

# MongoDB
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb/brew/mongodb-community

# Redis
brew install redis
brew services start redis
```

### Database Setup
```bash
# PostgreSQL
createdb booking_db
psql booking_db -c "CREATE USER booking_user WITH PASSWORD 'booking123';"
psql booking_db -c "GRANT ALL PRIVILEGES ON DATABASE booking_db TO booking_user;"

# MongoDB & Redis start automatically
```

### Run Application
```bash
# Development (H2 database)
./mvnw spring-boot:run -Dspring.profiles.active=dev

# Production (PostgreSQL + MongoDB + Redis)
./mvnw spring-boot:run
```

## 📊 Database Configuration

### PostgreSQL (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/booking_db
    username: booking_user
    password: booking123
```

### MongoDB
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/booking_catalog
```

### Redis
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
```

## 🧪 API Testing

### Sample Movie Data
```json
{
  "title": "Avengers",
  "description": "Superhero movie",
  "genre": "Action",
  "duration": 180,
  "language": "English",
  "releaseDate": "2023-01-01",
  "posterUrl": "http://example.com/poster.jpg",
  "rating": 8.5,
  "cast": ["Robert Downey Jr", "Chris Evans"]
}
```

### Test Endpoints
```bash
# Get all movies
curl http://localhost:8080/api/movies

# Add movie
curl -X POST http://localhost:8080/api/movies \
  -H "Content-Type: application/json" \
  -d '{"title":"Avengers","genre":"Action",...}'

# Search movies
curl "http://localhost:8080/api/movies/search?title=Avengers"
```

## 📁 Project Structure

```
src/main/java/com/seroter/MicroserviceBooking_app/
├── config/
│   ├── DatabaseConfig.java      # Multi-database configuration
│   └── SecurityConfig.java      # Security settings
├── controller/
│   ├── MovieController.java     # Movie API endpoints
│   └── TicketController.java    # Ticket operations
├── model/
│   ├── mongo/
│   │   └── MovieCatalog.java    # MongoDB document
│   ├── Booking.java             # JPA entities
│   ├── User.java
│   └── ...
├── repository/
│   ├── jpa/
│   │   ├── TicketRepository.java
│   │   └── UserRepository.java
│   └── mongo/
│       └── MovieCatalogRepository.java
└── service/
    ├── MovieService.java        # Business logic
    └── CacheService.java        # Redis operations
```

## 🎯 Next Steps

### Planned Features
- [ ] Booking management system
- [ ] Theater and show management
- [ ] Real-time seat availability
- [ ] Payment integration
- [ ] User authentication
- [ ] Search with Elasticsearch
- [ ] Event streaming with Kafka

### Performance Optimizations
- [ ] Database indexing
- [ ] Connection pooling
- [ ] Cache optimization
- [ ] API rate limiting

## 🔍 Database Tools

- **PostgreSQL**: DBeaver, pgAdmin
- **MongoDB**: MongoDB Compass, Studio 3T
- **Redis**: RedisInsight, Redis CLI

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📝 License

This project is licensed under the MIT License.

---

**Built with ❤️ using Spring Boot and BookMyShow's architecture principles**