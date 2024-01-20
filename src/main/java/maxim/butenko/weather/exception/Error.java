package maxim.butenko.weather.exception;

import lombok.Value;

@Value
public class Error {
    String code;
    String message;
}
