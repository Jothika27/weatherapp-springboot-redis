# Weather App

A Spring Boot REST API application that fetches weather information for cities using WeatherAPI.com. The application uses Redis with Sentinel for high-availability caching to improve performance and reduce external API calls.

## Technology Stack

- **Java**: 17
- **Spring Boot**: 4.0.1
- **Spring Web MVC**: REST API
- **Spring Data Redis**: Redis integration
- **Redis Sentinel**: High-availability Redis setup
- **Lombok**: Reducing boilerplate code
- **Docker Compose**: Container orchestration for Redis infrastructure
- **Maven**: Build tool

## Features

- RESTful API endpoint to fetch weather data by city name
- Redis caching with 10-minute TTL to reduce external API calls
- Redis Sentinel configuration for high availability
- Docker Compose setup for easy Redis infrastructure deployment

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+** (or use the included Maven wrapper `mvnw`)
- **Docker** and **Docker Compose** (for Redis setup)
- **WeatherAPI.com API Key** (already configured in `application.properties`, but you may want to use your own)

## Project Structure

```
weatherapp/
├── src/
│   ├── main/
│   │   ├── java/com/jo/weatherapp/
│   │   │   ├── controller/
│   │   │   │   └── WeatherController.java    # REST controller
│   │   │   ├── dto/
│   │   │   │   └── WeatherResponse.java      # Response DTO
│   │   │   ├── service/
│   │   │   │   └── WeatherService.java       # Business logic & API integration
│   │   │   └── WeatherappApplication.java    # Main application class
│   │   └── resources/
│   │       └── application.properties        # Application configuration
│   └── test/
├── docker/
│   ├── redis-master.conf                     # Redis master configuration
│   ├── redis-replica.conf                    # Redis replica configuration
│   └── sentinel.conf                         # Redis Sentinel configuration
├── docker-compose.yml                        # Docker Compose configuration
├── pom.xml                                   # Maven dependencies
└── README.md                                 # This file
```

## Setup Steps

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd weatherapp
```

### Step 2: Start Redis Infrastructure

The application uses Redis with Sentinel for high availability. Start the Redis services using Docker Compose:

```bash
docker-compose up -d
```

This command will start:

- **Redis Master** on port `6379`
- **Redis Replica** on port `6380`
- **Redis Sentinel** on port `26379`

To verify the containers are running:

```bash
docker-compose ps
```

### Step 3: Verify Redis Setup

You can verify that Redis Sentinel is properly configured by checking the logs:

```bash
docker-compose logs redis-sentinel
```

You should see messages indicating that Sentinel is monitoring the master.

### Step 4: Configure Application Properties (Optional)

The application is pre-configured with a WeatherAPI.com key. If you want to use your own API key:

1. Get a free API key from [WeatherAPI.com](https://www.weatherapi.com/)
2. Update `src/main/resources/application.properties`:

```properties
weather.api.key=your-api-key-here
```

### Step 5: Build the Application

Using Maven wrapper (recommended):

**Windows:**

```bash
.\mvnw.cmd clean install
```

**Linux/Mac:**

```bash
./mvnw clean install
```

Or using Maven directly (if installed):

```bash
mvn clean install
```

### Step 6: Run the Application

Using Maven wrapper:

**Windows:**

```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**

```bash
./mvnw spring-boot:run
```

Or using Maven directly:

```bash
mvn spring-boot:run
```

Or run the JAR file:

```bash
java -jar target/weatherapp-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**

## API Usage

### Get Weather by City

**Endpoint:** `GET /weather/{city}`

**Example Request:**

```bash
curl http://localhost:8080/weather/London
```

**Example Response:**

```json
{
  "city": "London",
  "temperature": 15.5,
  "condition": "Partly cloudy",
  "humidity": 65,
  "windSpeed": 12.3
}
```

### Response Fields

- `city`: Name of the city
- `temperature`: Temperature in Celsius
- `condition`: Weather condition description
- `humidity`: Humidity percentage
- `windSpeed`: Wind speed in km/h

## Caching

The application uses Redis caching with the following configuration:

- **Cache Name**: `weather`
- **TTL (Time To Live)**: 600,000 milliseconds (10 minutes)
- **Cache Key**: City name (case-insensitive)

When you request weather for a city, the first request will fetch data from WeatherAPI.com and cache it. Subsequent requests for the same city within 10 minutes will be served from the cache, improving response time and reducing API calls.

## Redis Sentinel Configuration

The application is configured to use Redis Sentinel for high availability:

- **Master Name**: `mymaster`
- **Sentinel Node**: `localhost:26379`
- **Failover**: Automatic failover configured with 5-second down detection and 10-second timeout

## Stopping the Application

1. **Stop the Spring Boot application**: Press `Ctrl+C` in the terminal

2. **Stop Redis containers**:

```bash
docker-compose down
```

To remove volumes as well:

```bash
docker-compose down -v
```

## Troubleshooting

### Redis Connection Issues

If you encounter Redis connection errors:

1. Verify Docker containers are running:

   ```bash
   docker-compose ps
   ```

2. Check Redis Sentinel logs:

   ```bash
   docker-compose logs redis-sentinel
   ```

3. Ensure the application can reach Redis on `localhost:26379`

### Port Already in Use

If port 8080 is already in use, update `server.port` in `application.properties`:

```properties
server.port=8081
```

### Weather API Errors

If you get errors from the Weather API:

1. Verify your API key is valid
2. Check your internet connection
3. Ensure the WeatherAPI.com service is available

## Development

### Running Tests

```bash
.\mvnw.cmd test
```

or

```bash
./mvnw test
```

### Building for Production

```bash
.\mvnw.cmd clean package
```

The JAR file will be created in the `target/` directory.

## License

This project is a demo application for educational purposes.
