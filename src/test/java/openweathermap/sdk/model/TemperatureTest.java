package openweathermap.sdk.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TemperatureTest {

    @Test
    void deserialisation() throws JsonProcessingException {
        var temperature = new Temperature(269.6,267.57);
        var temperatureAsString = new ObjectMapper().writeValueAsString(temperature);
        assertTrue(temperatureAsString.contains("feels_like"));
    }
}