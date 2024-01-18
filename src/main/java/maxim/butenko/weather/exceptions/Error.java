package maxim.butenko.weather.exceptions;

import lombok.Value;

@Value
public class Error {
    String code;
    String message;
}
