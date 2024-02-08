package maxim.butenko.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import maxim.butenko.weather.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class WeatherSessionDTO {
    UUID id;
    User user;
    LocalDateTime expiresAt;
}
