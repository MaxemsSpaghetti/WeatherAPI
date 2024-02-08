package maxim.butenko.weather.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.dto.LocationDTO;
import maxim.butenko.weather.entity.User;
import maxim.butenko.weather.service.LocationService;
import maxim.butenko.weather.service.WeatherService;
import maxim.butenko.weather.service.SessionService;
import maxim.butenko.weather.util.CookieHandler;
import maxim.butenko.weather.util.HtmlHelper;
import maxim.butenko.weather.util.UrlPath;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@WebServlet(UrlPath.AUTH_MAIN_PAGE)
public class AuthMainPageServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LocationService locationService = LocationService.getInstance();
    private final WeatherService weatherService = WeatherService.getInstance();
    private final CookieHandler cookieHandler = CookieHandler.getInstance();
    private final SessionService weatherSessionService = SessionService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Optional<Cookie> sessionCookie = cookieHandler.getSessionCookie(req);

        sessionCookie.map(Cookie::getValue)
                .map(UUID::fromString)
                .flatMap(weatherSessionService::findById)
                .ifPresent(sessionDTO -> {

                    if (sessionDTO.getExpiresAt().isBefore(LocalDateTime.now())) {

                        User user = sessionDTO.getUser();
                        List<LocationDTO> locationDTOS = locationService.findByUser(user);
                        req.setAttribute("locations", locationDTOS);

                        String input = req.getParameter("input");

                        try {
                            String location = weatherService.find(input);
                            LocationDTO locationDTO = objectMapper.readValue(location, LocationDTO.class);
                            locationService.add(locationDTO);
                            req.setAttribute("location", locationDTO);
                        } catch (InterruptedException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
//        if (sessionCookie.isPresent()) {
//            String sessionId = sessionCookie.get().getValue();
//            log.info("Cookie found: {}", sessionId);
//
//            UUID uuid = UUID.fromString(sessionId);
//            Optional<WeatherSessionDTO> weatherSessionDTO = weatherSessionService.findById(uuid);
//            log.info("Session found: {}", weatherSessionDTO);
//            if (weatherSessionDTO.isPresent()) {
//
//                WeatherSessionDTO sessionDTO = weatherSessionDTO.get();
//                User user = sessionDTO.getUser();
//                log.info("Found locations of user: {}", user);
//
//                List<LocationDTO> locationDTOS = locationService.findByUser(user);
//                req.setAttribute("locations", locationDTOS);
//                log.info("Displayed a list of locations: {}", locationDTOS);
//
//                String input = req.getParameter("input");
//
//                try {
//                    String location = weatherService.find(input);
//                    LocationDTO locationDTO = objectMapper.readValue(location, LocationDTO.class);
//
//                    locationService.add(locationDTO);
//                    req.setAttribute("location", locationDTO);
//
//                    log.info("Displayed a location: {}", locationDTO);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(HtmlHelper.getHtmlPath(UrlPath.AUTH_MAIN_PAGE)).forward(req, resp);
    }
}
