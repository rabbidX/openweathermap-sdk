package openweathermap.sdk;

import openweathermap.sdk.config.SDKConfig;
import openweathermap.sdk.exception.WeatherApiException;
import openweathermap.sdk.model.geocoding.GeocodingResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WeatherSDKOnDemandTest extends BaseTest{

    private WeatherSDK weatherSDK;

    @BeforeEach
    void setUp() {
        var config = SDKConfig.builder()
                .apiKey(API_KEY)
                .mode(SDKMode.ON_DEMAND)
                .build();

        weatherSDK = createSDKWithMockedClients(config);
    }

    @AfterEach
    void deleteSDK() {
        WeatherSDK.deleteSDK(API_KEY);
    }

    @Test
    void getCurrentWeather_shouldReturnWeatherData_whenCityExists() {
        var cityName = "London";
        var geoResponse = createGeocodingResponse();
        var oneCallResponse = createOneCallResponse();

        when(geocodingClient.getCoordinates(cityName))
                .thenReturn(List.of(geoResponse));
        when(oneCallClient.getCurrentWeather(anyDouble(), anyDouble()))
                .thenReturn(oneCallResponse);

        var result = weatherSDK.getCurrentWeather(cityName);

        assertNotNull(result);
        assertEquals("London", result.getName());
        assertEquals(10000, result.getVisibility());
        assertEquals(1684929490L, result.getDatetime());

        var weather = result.getWeather();
        assertNotNull(weather);
        assertEquals("broken clouds", weather.getDescription());
        assertEquals("Clouds", weather.getMain());

        var temperature = result.getTemperature();
        assertNotNull(temperature);
        assertEquals(292.55, temperature.getTemp());
        assertEquals(292.87, temperature.getFeelsLike());

        var wind = result.getWind();
        assertNotNull(wind);
        assertEquals(3.13, wind.getSpeed());

        var sys = result.getSys();
        assertNotNull(sys);
        assertEquals(1684926645L, sys.getSunrise());
        assertEquals(1684977332L, sys.getSunset());

        verify(geocodingClient).getCoordinates(cityName);
        verify(oneCallClient).getCurrentWeather(51.5073219, -0.1276474);
    }

    @Test
    void getCurrentWeather_shouldUseCache_whenDataIsFresh() {
        var cityName = "London";
        var geoResponse = createGeocodingResponse();
        var oneCallResponse = createOneCallResponse();

        when(geocodingClient.getCoordinates(cityName))
                .thenReturn(List.of(geoResponse));
        when(oneCallClient.getCurrentWeather(anyDouble(), anyDouble()))
                .thenReturn(oneCallResponse);

        // Save value to cache
        var firstCall = weatherSDK.getCurrentWeather(cityName);

        // Use value form cache
        var secondCall = weatherSDK.getCurrentWeather(cityName);

        assertNotNull(secondCall);
        assertEquals(firstCall, secondCall);
        // Only one call for each client
        verify(geocodingClient, times(1)).getCoordinates(cityName);
        verify(oneCallClient, times(1)).getCurrentWeather(anyDouble(), anyDouble());
    }

    @Test
    void getCurrentWeather_shouldThrowException_whenCityNotFound() {
        var cityName = "UnknownCity";
        when(geocodingClient.getCoordinates(cityName))
                .thenReturn(List.of());

        assertThrows(WeatherApiException.class, () -> weatherSDK.getCurrentWeather(cityName));

        verify(oneCallClient, never()).getCurrentWeather(anyDouble(), anyDouble());
    }

    @Test
    void getCurrentWeather_shouldThrowException_whenOneCallApiFails() {
        var cityName = "London";
        GeocodingResponse geoResponse = createGeocodingResponse();

        when(geocodingClient.getCoordinates(cityName))
                .thenReturn(List.of(geoResponse));
        when(oneCallClient.getCurrentWeather(anyDouble(), anyDouble()))
                .thenThrow(new WeatherApiException("API error"));

        assertThrows(WeatherApiException.class, () -> weatherSDK.getCurrentWeather(cityName));
    }

    @Test
    void onDemandMode_shouldNotStartPolling() throws InterruptedException {
        when(geocodingClient.getCoordinates(anyString()))
                .thenReturn(List.of(createGeocodingResponse()));
        when(oneCallClient.getCurrentWeather(anyDouble(), anyDouble()))
                .thenReturn(createOneCallResponse());

        weatherSDK.getCurrentWeather("Any city");
        weatherSDK.start();
        Thread.sleep(1500);
        verify(geocodingClient, times(1)).getCoordinates(anyString());
        verify(oneCallClient, times(1)).getCurrentWeather(anyDouble(), anyDouble());
    }
}