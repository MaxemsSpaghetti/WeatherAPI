package maxim.butenko.weather.service;

import maxim.butenko.weather.dao.SessionDAO;
import maxim.butenko.weather.dto.UserDTO;
import maxim.butenko.weather.dto.WeatherSessionDTO;
import maxim.butenko.weather.entity.User;
import maxim.butenko.weather.entity.WeatherSession;
import maxim.butenko.weather.utils.CustomMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.modelmapper.Converters.Collection.map;

public class WeatherSessionService {

   private final SessionDAO sessionDAO = SessionDAO.getInstance();
    private static final WeatherSessionService INSTANCE = new WeatherSessionService();

    private static final CustomMapper customMapper = CustomMapper.getInstance();

    public static WeatherSessionService getInstance() {
        return INSTANCE;
    }

    private WeatherSessionService() {

    }

    public Optional<WeatherSessionDTO> add(UserDTO userDTO) {
        User user = customMapper.convert(userDTO, User.class);
        WeatherSession session = buildWeatherSession(UUID.randomUUID(), user, LocalDateTime.now().plusDays(1));
        Optional<WeatherSession> weatherSession = sessionDAO.add(session);
        return Optional.ofNullable(weatherSession).map(weatherSes -> customMapper.convert(weatherSes, WeatherSessionDTO.class));
    }

    public Optional<WeatherSessionDTO> findById(UUID id) {
        return sessionDAO.findById(id)
                .map(weatherSession -> customMapper.convert(weatherSession, WeatherSessionDTO.class));
    }

    public void delete(UUID id) {
        sessionDAO.delete(id);
    }

    private WeatherSession buildWeatherSession(UUID id, User user, LocalDateTime expiresAt) {
        return WeatherSession.builder()
                .id(id)
                .user(user)
                .expiresAt(expiresAt)
                .build();
    }
}
