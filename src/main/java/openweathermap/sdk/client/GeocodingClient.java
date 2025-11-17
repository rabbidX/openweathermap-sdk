package openweathermap.sdk.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import openweathermap.sdk.config.SDKConfig;
import openweathermap.sdk.exception.WeatherApiException;
import openweathermap.sdk.model.geocoding.GeocodingResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.net.URIBuilder;

public class GeocodingClient {
    private final HttpClientExecutor executor;
    private final String apiKey;
    private final String baseUrl;

    public GeocodingClient(SDKConfig config) {
        this.executor = new HttpClientExecutor(HttpClients.createDefault(), new ObjectMapper());
        this.apiKey = config.getApiKey();
        this.baseUrl = config.getBaseUrl();
    }

    public List<GeocodingResponse> getCoordinates(String cityName) {
        try {
            URI uri = new URIBuilder(baseUrl + "/geo/1.0/direct")
                    .addParameter("q", cityName)
                    .addParameter("limit", "1") //only one location will be returned
                    .addParameter("appid", apiKey)
                    .build();

             GeocodingResponse[] responses = executor.executeGet(uri, GeocodingResponse[].class, "Geocoding API");
            return Arrays.asList(responses);
        } catch (URISyntaxException e) {
            throw new WeatherApiException("Invalid URI", e);
        }
    }
}