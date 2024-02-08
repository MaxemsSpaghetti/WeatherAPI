package maxim.butenko.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import maxim.butenko.weather.entity.locationDTOEntities.*;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {

    @JsonProperty("coord")
    Coordinates coordinates;

    List<Weather> weather;

    Main main;

    Integer visibility;

    Wind wind;

    Clouds clouds;

    @JsonProperty("sys")
    SunLightTime sunLightTime;

    LocalDateTime time;

    @JsonProperty("name")
    String city;
}
