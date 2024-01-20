package maxim.butenko.weather.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import maxim.butenko.weather.entity.Role;

import java.math.BigInteger;

@Value
@Builder
public class UserDTO {
    BigInteger id;
    String login;
    String password;
    Role role;
}
