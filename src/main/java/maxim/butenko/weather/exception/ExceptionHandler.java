package maxim.butenko.weather.exception;

import lombok.Getter;

import java.util.List;

public class ExceptionHandler extends RuntimeException {

    @Getter
    private final List<Error> errors;

    public ExceptionHandler(List<Error> errors) {
        this.errors = errors;
    }
}
