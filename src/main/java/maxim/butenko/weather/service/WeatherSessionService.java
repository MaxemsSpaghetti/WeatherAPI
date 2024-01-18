package maxim.butenko.weather.service;

import maxim.butenko.weather.dao.SessionDAO;
import maxim.butenko.weather.dto.WeatherSessionDTO;
import maxim.butenko.weather.entity.WeatherSession;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class WeatherSessionService {

   private final SessionDAO sessionDAO = SessionDAO.getInstance();
    private static final WeatherSessionService INSTANCE = new WeatherSessionService();

    private static final ModelMapper modelMapper = new ModelMapper();

    public static WeatherSessionService getInstance() {
        return INSTANCE;
    }

    private WeatherSessionService() {

    }

    public Optional<WeatherSessionDTO> add(Long userId) {
        WeatherSession session = buildWeatherSession(UUID.randomUUID(), userId, LocalDateTime.now().plusDays(1));
        return sessionDAO.add(session).map(weatherSession -> modelMapper.map(weatherSession, WeatherSessionDTO.class));
    }

    public Optional<WeatherSessionDTO> findById(UUID id) {
        return sessionDAO.findById(id).map(weatherSession -> modelMapper.map(weatherSession, WeatherSessionDTO.class));
    }

    public void delete(UUID id) {
        sessionDAO.delete(id);
    }

    private WeatherSession buildWeatherSession(UUID id, Long userId, LocalDateTime expiresAt) {
        return WeatherSession.builder()
                .id(id)
                .userId(userId)
                .expiresAt(expiresAt)
                .build();
    }
}
