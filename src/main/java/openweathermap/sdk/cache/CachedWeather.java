package openweathermap.sdk.cache;

import lombok.Value;
import openweathermap.sdk.model.WeatherData;

@Value
public class CachedWeather {
    private WeatherData data;
    private long timestamp; // cache creation timestamp

    public boolean isFresh(long ttlMillis) {
        long currentTime = System.currentTimeMillis();
        long dataAge = currentTime - timestamp;
        return dataAge < ttlMillis;
    }
}