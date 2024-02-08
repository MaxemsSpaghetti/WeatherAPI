package maxim.butenko.weather.entity.locationDTOEntities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Clouds {
    @JsonProperty("all")
    private Integer clouds;
}
