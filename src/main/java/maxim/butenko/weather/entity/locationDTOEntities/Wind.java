package maxim.butenko.weather.entity.locationDTOEntities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind {

    @JsonProperty("speed")
    Double windSpeed;

    @JsonProperty("deg")
    Double windDeg;

    @JsonProperty("gust")
    Double windGust;
}
