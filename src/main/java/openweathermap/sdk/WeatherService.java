package openweathermap.sdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import openweathermap.sdk.cache.WeatherCache;
import openweathermap.sdk.client.GeocodingClient;
import openweathermap.sdk.client.OneCallClient;
import openweathermap.sdk.config.SDKConfig;
import openweathermap.sdk.exception.WeatherApiException;
import openweathermap.sdk.model.WeatherData;
import java.util.concurrent.TimeUnit;

class WeatherService {
    private final GeocodingClient geocodingClient;
    private final OneCallClient oneCallClient;
    private final WeatherCache cache;
    private final SDKConfig config;
    private ScheduledExecutorService scheduler;

    public WeatherService(SDKConfig config) {
        this.config = config;
        this.geocodingClient = new GeocodingClient(config);
        this.oneCallClient = new OneCallClient(config);
        this.cache = new WeatherCache();
    }

    public WeatherData getCurrentWeather(String cityName) {
        var cached = cache.get(cityName);
        if (cached != null) {
            return cached;
        }
        return getCurrentWeatherWithoutCheckingCache(cityName);
    }

    public void startPolling() {
        if (scheduler != null && !scheduler.isShutdown()) {
            return; // Already started
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();
        final long rate = config.getPollingRateInSeconds() > 0 ?
                config.getPollingRateInSeconds() : 60L;

        scheduler.scheduleAtFixedRate(
                this::updateAllCachedCities,
                0,
                rate,
                TimeUnit.SECONDS
        );
    }

    public void shutdown() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    private void updateAllCachedCities() {
        List<String> cities;
        synchronized (cache) {
            cities = new ArrayList<>(cache.getCache().keySet());
        }
        cities.forEach(this::getCurrentWeatherWithoutCheckingCache);
    }

    private WeatherData getCurrentWeatherWithoutCheckingCache(String cityName) {
        var geo = geocodingClient.getCoordinates(cityName);
        if (geo.isEmpty()) {
            throw new WeatherApiException("Cannot get coordinates for city: " + cityName);
        }

        var location = geo.getFirst();
        var weather = oneCallClient.getCurrentWeather(location.getLat(), location.getLon());

        var result = new WeatherData(weather, location);
        cache.put(cityName, result);

        return result;
    }
}
