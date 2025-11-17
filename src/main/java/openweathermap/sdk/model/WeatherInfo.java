package openweathermap.sdk.model;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import openweathermap.sdk.model.onecall.Weather;

@Getter
@EqualsAndHashCode
public class WeatherInfo {
    private String main;
    private String description;

    public WeatherInfo(List<Weather> weather) {
        if (weather == null || weather.isEmpty()) {
            this.main = "Unknown";
            this.description = "No weather data";
            return;
        }

        Weather firstWeather = weather.getFirst();
        this.main = firstWeather.getMain();
        this.description = firstWeather.getDescription();
    }
}
