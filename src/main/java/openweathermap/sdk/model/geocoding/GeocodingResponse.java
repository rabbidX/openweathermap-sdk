package openweathermap.sdk.model.geocoding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GeocodingResponse {
    private String name;
    private double lat;
    private double lon;
}
