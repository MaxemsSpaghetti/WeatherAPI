package maxim.butenko.weather.servlet;

import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.dto.WeatherSessionDTO;
import maxim.butenko.weather.service.SessionService;
import maxim.butenko.weather.util.CookieHandler;
import maxim.butenko.weather.util.UrlPath;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@WebServlet(UrlPath.SIGN_OUT)
public class SignOutServlet extends HttpServlet{

    private static final SessionService sessionService = SessionService.getInstance();

    public static final CookieHandler cookieHandler = CookieHandler.getInstance();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Optional<Cookie> cookie = cookieHandler.getSessionCookie(req);

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
            resp.sendRedirect(UrlPath.SIGN_IN);

        }
    }
}
