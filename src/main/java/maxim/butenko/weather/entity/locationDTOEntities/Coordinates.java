package maxim.butenko.weather.entity.locationDTOEntities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinates {

    @JsonProperty("lon")
    private Double latitude;

    @JsonProperty("lat")
    private Double longitude;
}
