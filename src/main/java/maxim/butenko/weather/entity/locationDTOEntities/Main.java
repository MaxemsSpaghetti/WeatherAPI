package maxim.butenko.weather.entity.locationDTOEntities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {

    @JsonProperty("temp")
    private Double temperature;

    @JsonProperty("feels_like")
    private Double temperatureFeelsLike;

    @JsonProperty("temp_min")
    private Double temperatureMin;

    @JsonProperty("temp_max")
    private Double temperatureMax;

    @JsonProperty("pressure")
    private Integer pressure;

    @JsonProperty("humidity")
    private Integer humidity;


}
