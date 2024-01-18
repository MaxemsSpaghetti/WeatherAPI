package maxim.butenko.weather.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.dto.UserDTO;
import maxim.butenko.weather.dto.WeatherSessionDTO;
import maxim.butenko.weather.entity.WeatherSession;
import maxim.butenko.weather.service.WeatherSessionService;
import maxim.butenko.weather.service.UserService;
import maxim.butenko.weather.utils.HtmlHelper;
import maxim.butenko.weather.utils.UrlPath;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@WebServlet(UrlPath.LOGIN)
public class LoginServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    private static final WeatherSessionService sessionService = WeatherSessionService.getInstance();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        String bCryptPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        Optional<UserDTO> optionalUserDTO = userService.findByLoginAndPassword(login, bCryptPassword);

        if (optionalUserDTO.isPresent()) {

            Optional<WeatherSessionDTO> optionalWeatherSessionDTO = sessionService.add(optionalUserDTO.get().getId());

            if (optionalWeatherSessionDTO.isPresent()) {

                WeatherSessionDTO session = optionalWeatherSessionDTO.get();
                log.info("Session created: {}", session.getId());

                Cookie cookie = new Cookie("session", session.getId().toString());
                resp.addCookie(cookie);
                log.info("Cookie created: {}", cookie.getValue());

                log.info("User successfully logged in: {}", login);
                resp.sendRedirect(HtmlHelper.getHtmlPath(UrlPath.WEATHER));
            }
        } else {
            resp.sendRedirect(HtmlHelper.getHtmlPath(UrlPath.LOGIN));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(HtmlHelper.getHtmlPath(UrlPath.LOGIN)).forward(req, resp);
    }
}
