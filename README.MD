# OpenWeatherMap SDK

A Java SDK for accessing OpenWeatherMap API with built-in caching and polling support.

## Features

- **Easy to use** - Simple API for weather data retrieval
- **Smart caching** - LRU cache with TTL (10 minutes default)
- **Dual modes** - On-demand and polling modes
- **Thread-safe** - Designed for concurrent usage
- **Error handling** - Comprehensive exception management

## Installation

### Gradle
```gradle
dependencies {
    implementation 'openweathermap.sdk:openweathermap-sdk:1.0.0'
}
```

### Maven
```xml
<dependency>
    <groupId>openweathermap.sdk</groupId>
    <artifactId>openweathermap-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Quick Start
```java
// Create SDK configuration
SDKConfig config = SDKConfig.builder()
    .apiKey("your-api-key")
    .mode(SDKMode.ON_DEMAND)
    .build();
// Create SDK instance
WeatherSDK sdk = WeatherSDK.createSDK(config);

// Get current weather
WeatherData weather = sdk.getCurrentWeather("London");

// Use weather data
System.out.println("Temperature: " + weather.getTemperature().getTemp());
System.out.println("Conditions: " + weather.getWeather().getDescription());
```
## Configuration

### SDKConfig Builder Options
```java
SDKConfig config = SDKConfig.builder()
    .apiKey("your-api-key")           // Required
    .mode(SDKMode.POLLING)            // ON_DEMAND or POLLING
    .pollingRateInSeconds(300)        // 5 minutes (POLLING mode only)
    .baseUrl("https://api.openweathermap.org") // Custom API endpoint
    .build();
```
### SDK Modes
ON_DEMAND - Fetch data only when requested

POLLING - Background updates for zero-latency responses

## Usage Examples

## Basic Usage
```java
SDKConfig config = SDKConfig.builder()
    .apiKey("your-api-key")
    .build();

WeatherSDK sdk = WeatherSDK.createSDK(config);
WeatherData weather = sdk.getCurrentWeather("Paris");
```

### Polling Mode
```java
SDKConfig config = SDKConfig.builder()
        .apiKey("your-api-key")
        .mode(SDKMode.POLLING)
        .pollingRateInSeconds(300) // Update every 5 minutes
        .build();

WeatherSDK sdk = WeatherSDK.createSDK(config);
sdk.start(); // Start background updates

// Use SDK - data will be fresh from cache
WeatherData weather = sdk.getCurrentWeather("Berlin");

sdk.shutdown(); // Stop background updates
```

### Instance Management
```java
// Create SDK instances for different API keys
SDKConfig config1 = SDKConfig.builder().apiKey("key1").build();
SDKConfig config2 = SDKConfig.builder().apiKey("key2").build();

WeatherSDK sdk1 = WeatherSDK.createSDK(config1);
WeatherSDK sdk2 = WeatherSDK.createSDK(config2);

// Get existing instance
WeatherSDK existingSdk = WeatherSDK.getSDK("key1");

// Delete instance when done
WeatherSDK.deleteSDK("key1");
```

### Response Format
```json
{
  "weather": {
    "main": "Clouds",
    "description": "scattered clouds"
  },
  "temperature": {
    "temp": 269.6,
    "feels_like": 267.57
  },
  "visibility": 10000,
  "wind": {
    "speed": 1.38
  },
  "datetime": 1675744800,
  "sys": {
    "sunrise": 1675751262,
    "sunset": 1675787560
  },
  "timezone": 3600,
  "name": "Zocca"
}
```

### Error Handling
```java
try {
    WeatherData weather = sdk.getCurrentWeather("UnknownCity");
} catch (WeatherApiException e) {
    System.out.println("API Error: " + e.getMessage());
}
```

### Cache Behavior
Max cities: 10 

TTL: 10 minutes

Eviction: LRU (Least Recently Used)

Thread-safe: Concurrent access supported

### Instance Management
One instance per API key - Guaranteed by SDK

Automatic cleanup - Call deleteSDK() to release resources

Thread-safe access - Safe for concurrent usage

### Dependencies
Apache HttpClient 5

Jackson JSON Processor

Lombok (compile time)

### Requirements
Java 17 or higher

OpenWeatherMap API key

### Getting API Key
Sign up at OpenWeatherMap

Navigate to API keys section

Copy your API key
