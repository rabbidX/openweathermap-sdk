package openweathermap.sdk.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import openweathermap.sdk.config.SDKConfig;
import openweathermap.sdk.exception.WeatherApiException;
import openweathermap.sdk.model.onecall.OneCallResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.net.URIBuilder;

public class OneCallClient {
    private final HttpClientExecutor executor;
    private final String apiKey;
    private final String baseUrl;

    public OneCallClient(SDKConfig config) {
        this.executor = new HttpClientExecutor(HttpClients.createDefault(), new ObjectMapper());
        this.apiKey = config.getApiKey();
        this.baseUrl = config.getBaseUrl();
    }

    public OneCallResponse getCurrentWeather(double lat, double lon) {
        try {
            URI uri = new URIBuilder(baseUrl + "/data/3.0/onecall")
                    .addParameter("lat", String.valueOf(lat))
                    .addParameter("lon", String.valueOf(lon))
                    .addParameter("exclude", "minutely,hourly,daily,alerts")
                    .addParameter("appid", apiKey)
                    .build();

            return executor.executeGet(uri, OneCallResponse.class, "OneCall API");
        } catch (URISyntaxException e) {
            throw new WeatherApiException("Invalid URI", e);
         }
    }
}