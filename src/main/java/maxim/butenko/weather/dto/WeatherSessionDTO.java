package maxim.butenko.weather.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class WeatherSessionDTO {
    UUID id;
    Long userId;
    LocalDateTime expiresAt;
}
