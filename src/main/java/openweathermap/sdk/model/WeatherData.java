package openweathermap.sdk.model;

import lombok.Builder;
import lombok.Getter;
import openweathermap.sdk.exception.WeatherApiException;
import openweathermap.sdk.model.geocoding.GeocodingResponse;
import openweathermap.sdk.model.onecall.OneCallResponse;

@Getter
public class WeatherData {
    private WeatherInfo weather;
    private Temperature temperature;
    private int visibility;
    private Wind wind;
    private long datetime;
    private Sys sys;
    private int timezone;
    private String name;

    public WeatherData(OneCallResponse oneCall, GeocodingResponse geo) {
        if (oneCall.getCurrent() == null) {
            throw new WeatherApiException("Current weather was not received");
        }
        this.weather = new WeatherInfo(oneCall.getCurrent().getWeather());
        this.temperature = new Temperature(oneCall.getCurrent().getTemp(), oneCall.getCurrent().getFeelsLike());
        this.visibility = oneCall.getCurrent().getVisibility();
        this.wind = new Wind(oneCall.getCurrent().getWindSpeed());
        this.datetime = oneCall.getCurrent().getDateTime();
        this.sys = new Sys(oneCall.getCurrent().getSunrise(), oneCall.getCurrent().getSunset());
        this.timezone = oneCall.getCurrent().getTimezone();
        this.name = geo.getName();
    }
}