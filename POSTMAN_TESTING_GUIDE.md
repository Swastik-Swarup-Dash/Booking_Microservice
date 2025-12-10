# Postman Testing Guide for Booking Microservice

## 🚀 Quick Start

### 1. Import Collection
- Open Postman
- Click **Import** → **Upload Files**
- Select `Booking-Microservice.postman_collection.json`

### 2. Start Application
```bash
cd /Users/ishan/Developer/MicroserviceBooking-app
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### 3. Verify Application is Running
- Test: `GET http://localhost:8080/actuator/health`
- Expected: `{"status":"UP"}`

## 📋 Test Scenarios

### Movie API Tests

#### 1. Get All Movies (Empty Database)
```
GET http://localhost:8080/api/movies
Expected: [] (empty array)
```

#### 2. Add First Movie
```
POST http://localhost:8080/api/movies
Content-Type: application/json

{
  "title": "Avengers: Endgame",
  "description": "The epic conclusion to the Infinity Saga",
  "genre": "Action",
  "duration": 181,
  "language": "English",
  "releaseDate": "2019-04-26",
  "posterUrl": "https://example.com/avengers-endgame.jpg",
  "rating": 8.4,
  "cast": ["Robert Downey Jr.", "Chris Evans", "Mark Ruffalo"]
}

Expected: 201 Created with movie object
```

#### 3. Add More Test Movies
```json
{
  "title": "Spider-Man: No Way Home",
  "description": "Spider-Man multiverse adventure",
  "genre": "Action",
  "duration": 148,
  "language": "English",
  "releaseDate": "2021-12-17",
  "posterUrl": "https://example.com/spiderman.jpg",
  "rating": 8.2,
  "cast": ["Tom Holland", "Zendaya", "Benedict Cumberbatch"]
}
```

```json
{
  "title": "The Batman",
  "description": "Dark Knight detective story",
  "genre": "Action",
  "duration": 176,
  "language": "English",
  "releaseDate": "2022-03-04",
  "posterUrl": "https://example.com/batman.jpg",
  "rating": 7.8,
  "cast": ["Robert Pattinson", "Zoë Kravitz", "Paul Dano"]
}
```

```json
{
  "title": "Dune",
  "description": "Epic sci-fi adaptation",
  "genre": "Sci-Fi",
  "duration": 155,
  "language": "English",
  "releaseDate": "2021-10-22",
  "posterUrl": "https://example.com/dune.jpg",
  "rating": 8.0,
  "cast": ["Timothée Chalamet", "Rebecca Ferguson", "Oscar Isaac"]
}
```

#### 4. Test Search & Filter
```
GET http://localhost:8080/api/movies/genre/Action
Expected: Action movies only

GET http://localhost:8080/api/movies/genre/Sci-Fi
Expected: Sci-Fi movies only

GET http://localhost:8080/api/movies/search?title=Spider
Expected: Spider-Man movie

GET http://localhost:8080/api/movies/search?title=Avengers
Expected: Avengers movie
```

## 🧪 Test Validation

### Success Criteria
- ✅ Health check returns UP status
- ✅ Empty movies list initially
- ✅ Movies can be added successfully
- ✅ Movies list shows added movies
- ✅ Genre filtering works
- ✅ Title search works
- ✅ Redis caching is active (check logs)

### Error Testing
```
POST http://localhost:8080/api/movies
Content-Type: application/json

{
  "title": "",
  "genre": "Action"
}

Expected: 400 Bad Request (validation error)
```

## 🔍 Database Verification

### MongoDB (Movie Catalog)
```bash
# Connect to MongoDB
mongosh booking_catalog

# Check collections
show collections

# View movies
db.movieCatalog.find().pretty()
```

### Redis (Cache)
```bash
# Connect to Redis
redis-cli

# Check cached data
KEYS *
GET "movies::SimpleKey []"
```

## 📊 Performance Testing

### Load Test with Multiple Requests
1. Create 10+ movies using POST requests
2. Test concurrent GET requests
3. Verify cache performance improvement
4. Monitor application logs for cache hits/misses

## 🐛 Troubleshooting

### Common Issues
- **Port 8080 in use**: Change server.port in application.yml
- **Database connection**: Verify MongoDB/Redis are running
- **404 errors**: Check if application started successfully
- **Validation errors**: Verify JSON format and required fields

### Debug Commands
```bash
# Check if services are running
brew services list | grep -E "(mongodb|redis|postgresql)"

# View application logs
tail -f logs/application.log

# Test database connections
mongosh --eval "db.runCommand('ping')"
redis-cli ping
```

## 📈 Next Steps

After basic testing works:
1. Test with PostgreSQL profile (production)
2. Add authentication headers
3. Test booking endpoints (when implemented)
4. Performance testing with larger datasets
5. Error handling validation