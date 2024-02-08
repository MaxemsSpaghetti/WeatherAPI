package maxim.butenko.weather.util;

import maxim.butenko.weather.dto.WeatherSessionDTO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class CookieHandler {

    private static final CookieHandler INSTANCE = new CookieHandler();

    private CookieHandler() {
    }

    public static CookieHandler getInstance() {
        return INSTANCE;
    }

    public Optional<Cookie> getSessionCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (nonNull(cookies)) {
            return  Arrays.stream(cookies)
                    .filter(c -> c.getName().equals("sessionId"))
                    .findFirst();
        }

        return Optional.empty();
    }

    public Cookie createSessionCookie(WeatherSessionDTO sessionDTO) {
        return new Cookie("sessionId", sessionDTO.getId().toString());
    }
}
