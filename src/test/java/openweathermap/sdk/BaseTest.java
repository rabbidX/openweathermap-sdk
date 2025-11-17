package openweathermap.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import openweathermap.sdk.client.GeocodingClient;
import openweathermap.sdk.client.OneCallClient;
import openweathermap.sdk.config.SDKConfig;
import openweathermap.sdk.model.geocoding.GeocodingResponse;
import openweathermap.sdk.model.onecall.OneCallResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public abstract class BaseTest {
    protected final String API_KEY = "test-api-key";
    @Mock
    protected GeocodingClient geocodingClient;
    @Mock
    protected OneCallClient oneCallClient;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected WeatherSDK createSDKWithMockedClients(SDKConfig config) {
        try {
            WeatherSDK sdk = WeatherSDK.createSDK(config);

            var weatherServiceField = WeatherSDK.class.getDeclaredField("weatherService");
            weatherServiceField.setAccessible(true);

            var geocodingClientField = WeatherService.class.getDeclaredField("geocodingClient");
            geocodingClientField.setAccessible(true);

            var oneCallClientField = WeatherService.class.getDeclaredField("oneCallClient");
            oneCallClientField.setAccessible(true);

            WeatherService weatherService = (WeatherService) weatherServiceField.get(sdk);
            geocodingClientField.set(weatherService, geocodingClient);
            oneCallClientField.set(weatherService, oneCallClient);

            return sdk;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SDK with mocked clients", e);
        }
    }

    protected GeocodingResponse createGeocodingResponse() {
        File file = new File("src/test/resources/geocoding-response.json");
        try {
            return objectMapper.readValue(file, GeocodingResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected OneCallResponse createOneCallResponse() {
        File file = new File("src/test/resources/onecall-response.json");
        try {
            return objectMapper.readValue(file, OneCallResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
