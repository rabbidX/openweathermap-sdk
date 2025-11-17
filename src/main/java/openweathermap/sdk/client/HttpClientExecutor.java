package openweathermap.sdk.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import openweathermap.sdk.exception.WeatherApiException;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class HttpClientExecutor {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HttpClientExecutor(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public <T> T executeGet(URI uri, Class<T> valueType, String apiName) {
        try {
            HttpGet request = new HttpGet(uri);

            return httpClient.execute(request, response -> {
                int statusCode = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                if (statusCode == 200) {
                    return objectMapper.readValue(responseBody, valueType);
                } else {
                    throw new WeatherApiException(apiName + " error: " + statusCode + " - " + responseBody);
                }
            });

        } catch (IOException e) {
            throw new WeatherApiException("Network error", e);
        }
    }
}
