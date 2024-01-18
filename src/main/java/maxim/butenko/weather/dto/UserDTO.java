package maxim.butenko.weather.dto;

import lombok.Builder;
import lombok.Value;
import maxim.butenko.weather.entity.Role;

@Value
@Builder
public class UserDTO {
    Long id;
    String login;
    String password;
    Role role;
}
