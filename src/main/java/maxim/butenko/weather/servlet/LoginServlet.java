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
import maxim.butenko.weather.service.WeatherSessionService;
import maxim.butenko.weather.service.UserService;
import maxim.butenko.weather.util.HtmlHelper;
import maxim.butenko.weather.util.UrlPath;

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

        Optional<UserDTO> optionalUserDTO = userService.findByLogin(login);

        if (optionalUserDTO.isPresent()) {
            UserDTO userDTO = optionalUserDTO.get();

            boolean isValid = BCrypt.verifyer().verify(password.toCharArray(), userDTO.getPassword()).verified;

            if (!isValid) {
                resp.sendRedirect(HtmlHelper.getHtmlPath(UrlPath.LOGIN));
                return;
            }

            Optional<WeatherSessionDTO> optionalWeatherSessionDTO = sessionService.add(userDTO);

            if (optionalWeatherSessionDTO.isPresent()) {

                WeatherSessionDTO session = optionalWeatherSessionDTO.get();
                log.info("Session created: {}", session.getId());

                Cookie cookie = new Cookie("session", session.getId().toString());
                resp.addCookie(cookie);
                log.info("Cookie created: {}", cookie.getValue());

                log.info("User successfully logged in: {}", login);
                resp.sendRedirect(HtmlHelper.getHtmlPath(UrlPath.WEATHER));
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(HtmlHelper.getHtmlPath(UrlPath.LOGIN)).forward(req, resp);
    }
}
