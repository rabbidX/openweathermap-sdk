package openweathermap.sdk.cache;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import openweathermap.sdk.model.WeatherData;

public class WeatherCache {
    private final static int TIME_TO_LIVE = 10 * 60 * 1000; // 10 minutes
    @Getter
    private final Map<String, CachedWeather> cache = new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, CachedWeather> eldest) {
            return size() > 10; // Least Recently Used
        }
    };

    public WeatherData get(String city) {
        synchronized (cache) {
            CachedWeather cached = cache.get(city);
            if (cached != null && cached.isFresh(TIME_TO_LIVE)) {
                return cached.getData();
            }
            return null;
        }
    }

    public void put(String city, WeatherData data) {
        synchronized (cache) {
            cleanExpiredEntries(); // TTL cleaning
            cache.put(city, new CachedWeather(data, System.currentTimeMillis()));
        }
    }

    private void cleanExpiredEntries() {
        cache.entrySet().removeIf(entry ->
                !entry.getValue().isFresh(TIME_TO_LIVE)
        );
    }
}