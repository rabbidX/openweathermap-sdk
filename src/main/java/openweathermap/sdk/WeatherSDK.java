package openweathermap.sdk;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import openweathermap.sdk.config.SDKConfig;
import openweathermap.sdk.model.WeatherData;

public class WeatherSDK {
    private static final Map<String, WeatherSDK> instances = new ConcurrentHashMap<>();
    private final SDKConfig config;
    private final WeatherService weatherService;


    private WeatherSDK(SDKConfig config) {
        this.config = config;
        this.weatherService = new WeatherService(config);
    }

    //The main method for clients
    public WeatherData getCurrentWeather(String cityName) {
        return weatherService.getCurrentWeather(cityName);
    }

    public void start() {
        if (config.getMode() == SDKMode.POLLING) {
            weatherService.startPolling();
        }
    }

    public void shutdown() {
        weatherService.shutdown();
    }

    public static WeatherSDK createSDK(SDKConfig config) {
        return instances.computeIfAbsent(config.getApiKey(), key -> new WeatherSDK(config));
    }

    public static WeatherSDK getSDK(String apiKey) {
        return instances.get(apiKey);
    }

    public static void deleteSDK(String apiKey) {
        WeatherSDK instance = instances.remove(apiKey);
        if (instance != null) {
            instance.shutdown(); // graceful shutdown
        }
    }
}