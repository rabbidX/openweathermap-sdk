package openweathermap.sdk.config;

import java.time.Duration;
import lombok.Builder;
import lombok.Getter;
import openweathermap.sdk.SDKMode;

@Builder
@Getter
public class SDKConfig {
    private final String apiKey;
    private final SDKMode mode;
    private final String baseUrl;
    private final long pollingRateInSeconds;
}
