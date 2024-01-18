package maxim.butenko.weather.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sessions", schema = "weather")
public class WeatherSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
