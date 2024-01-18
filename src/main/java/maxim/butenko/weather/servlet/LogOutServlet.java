package maxim.butenko.weather.servlet;

import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.dto.WeatherSessionDTO;
import maxim.butenko.weather.service.WeatherSessionService;
import maxim.butenko.weather.utils.UrlPath;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@WebServlet(UrlPath.LOGOUT)
public class LogOutServlet extends HttpServlet{

    private final WeatherSessionService sessionService = WeatherSessionService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Cookie[] cookies = req.getCookies();
        Optional<Cookie> cookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("sessionId"))
                .findFirst();

        if (cookie.isPresent()) {

            String sessionId = cookie.get().getValue();
            log.info("Cookie found: {}", sessionId);

            UUID uuid = UUID.fromString(sessionId);
            Optional<WeatherSessionDTO> weatherSessionDTO = sessionService.findById(uuid);
            log.info("Session found: {}", weatherSessionDTO);

            weatherSessionDTO.ifPresent(sessionDTO -> sessionService.delete(sessionDTO.getId()));
            log.info("Session deleted: {}", weatherSessionDTO);

            req.getSession().invalidate();

            log.info("User is successfully logged out");
            resp.sendRedirect(UrlPath.LOGIN);

        }
    }
}
