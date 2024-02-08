package maxim.butenko.weather.util;

import maxim.butenko.weather.dto.LocationDTO;
import maxim.butenko.weather.dto.UserDTO;
import maxim.butenko.weather.dto.WeatherSessionDTO;
import maxim.butenko.weather.entity.Location;
import maxim.butenko.weather.entity.User;
import maxim.butenko.weather.entity.WeatherSession;
import org.modelmapper.ModelMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class CustomMapper extends ModelMapper {

    private static final CustomMapper INSTANCE = new CustomMapper();

    private CustomMapper() {
    }


    public static CustomMapper getInstance() {
        return INSTANCE;
    }

    public <T> T convert(Object source, Class<T> destinationType) {
        if (source instanceof Optional) {
            Optional<?> optional = (Optional<?>) source;
            if (optional.isPresent()) {
                Object value = optional.get();
                if (destinationType.isInstance(value)) {
                    return destinationType.cast(value);
                } else {
                    String conversionMethodName = "convert" + value.getClass().getSimpleName()
                            + "To" + destinationType.getSimpleName();
                    try {
                        Method conversionMethod = this.getClass()
                                .getDeclaredMethod(conversionMethodName, value.getClass());
                        return destinationType.cast(conversionMethod.invoke(this, value));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return super.map(source, destinationType);
    }

    private UserDTO convertUserToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    private WeatherSessionDTO convertWeatherSessionToWeatherSessionDTO(WeatherSession session) {
        return WeatherSessionDTO.builder()
                .id(session.getId())
                .user(session.getUser())
                .expiresAt(session.getExpiresAt())
                .build();
    }

    public Location convertLocationDTOToLocation(LocationDTO locationDTO, List<User> users) {
        return Location.builder()
                .name(locationDTO.getCity())
                .users(users)
                .latitude(locationDTO.getCoordinates().getLatitude())
                .longitude(locationDTO.getCoordinates().getLongitude())
                .build();
    }
}
