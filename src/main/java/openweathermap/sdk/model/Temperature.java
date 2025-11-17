package openweathermap.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Temperature {
    private double temp;
    @JsonProperty("feels_like")
    private double feelsLike;
}
