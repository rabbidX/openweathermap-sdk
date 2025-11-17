package openweathermap.sdk.model.onecall;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CurrentWeather {
    private List<Weather> weather;
    private double temp;
    @JsonProperty("feels_like")
    private double feelsLike;
    private int visibility;
    @JsonProperty("wind_speed")
    private double windSpeed;
    @JsonProperty("dt")
    private long dateTime;
    private long sunrise;
    private long sunset;
    @JsonProperty("timezone_offset")
    private int timezone;
}
