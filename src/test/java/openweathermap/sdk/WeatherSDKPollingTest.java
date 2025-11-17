package openweathermap.sdk;

import openweathermap.sdk.config.SDKConfig;
import openweathermap.sdk.model.geocoding.GeocodingResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WeatherSDKPollingTest extends BaseTest{

    private WeatherSDK weatherSDK;

    @BeforeEach
    void setUp() {
        var config = SDKConfig.builder()
                .apiKey(API_KEY)
                .mode(SDKMode.POLLING)
                .pollingRateInSeconds(1) //quick start
                .build();

        weatherSDK = createSDKWithMockedClients(config);
    }

    @AfterEach
    void deleteSDK() {
        WeatherSDK.deleteSDK(API_KEY);
    }

    @Test
    void pollingMode_shouldUpdateCachePeriodically() throws InterruptedException {
        var cityName = "London";
        var geoResponse = createGeocodingResponse();
        var oneCallResponse = createOneCallResponse();

        when(geocodingClient.getCoordinates(cityName))
                .thenReturn(List.of(geoResponse));
        when(oneCallClient.getCurrentWeather(anyDouble(), anyDouble()))
                .thenReturn(oneCallResponse);

        var result = weatherSDK.getCurrentWeather(cityName);
        assertNotNull(result);
        weatherSDK.start();
        Thread.sleep(2500);
        weatherSDK.shutdown();

        verify(geocodingClient, atLeast(3)).getCoordinates(cityName);
        verify(oneCallClient, atLeast(3)).getCurrentWeather(anyDouble(), anyDouble());
    }

    @Test
    void pollingMode_shouldUpdateMultipleCities() throws InterruptedException {
        when(geocodingClient.getCoordinates(anyString()))
                .thenAnswer(invocation -> {
                    String city = invocation.getArgument(0);
                    GeocodingResponse response = new GeocodingResponse();
                    response.setName(city);
                    response.setLat(50.0);
                    response.setLon(0.0);
                    return List.of(response);
                });

        when(oneCallClient.getCurrentWeather(anyDouble(), anyDouble()))
                .thenReturn(createOneCallResponse());

        weatherSDK.getCurrentWeather("London");
        weatherSDK.getCurrentWeather("Paris");
        weatherSDK.getCurrentWeather("Berlin");

        weatherSDK.start();
        Thread.sleep(1500);
        weatherSDK.shutdown();

        verify(geocodingClient, atLeast(2)).getCoordinates("London");
        verify(geocodingClient, atLeast(2)).getCoordinates("Paris");
        verify(geocodingClient, atLeast(2)).getCoordinates("Berlin");
    }
}