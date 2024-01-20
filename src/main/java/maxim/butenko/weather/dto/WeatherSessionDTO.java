package maxim.butenko.weather.dto;

import lombok.Builder;
import lombok.Value;
import maxim.butenko.weather.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class WeatherSessionDTO {
    UUID id;
    User user;
    LocalDateTime expiresAt;
}
