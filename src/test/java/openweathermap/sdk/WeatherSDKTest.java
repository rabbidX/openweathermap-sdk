package openweathermap.sdk;

import openweathermap.sdk.config.SDKConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class WeatherSDKTest {
    @Test
    void createDeleteSDK() {
        var apiKey = "my-key";
        var config1 = SDKConfig.builder()
                .apiKey(apiKey)
                .mode(SDKMode.ON_DEMAND)
                .build();
        var config2 = SDKConfig.builder()
                .apiKey(apiKey)
                .mode(SDKMode.POLLING)
                .build();
        var sdk1 = WeatherSDK.createSDK(config1);
        assertNotNull(sdk1);

        var sdk2 = WeatherSDK.createSDK(config2);
        assertNotNull(sdk2);
        assertSame(sdk1, sdk2);

        WeatherSDK.deleteSDK(apiKey);
        var sdk3 = WeatherSDK.getSDK(apiKey);
        assertNull(sdk3);
    }
}
