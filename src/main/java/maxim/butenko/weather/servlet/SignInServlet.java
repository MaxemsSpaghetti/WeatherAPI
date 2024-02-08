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
import maxim.butenko.weather.service.SessionService;
import maxim.butenko.weather.service.UserService;
import maxim.butenko.weather.util.CookieHandler;
import maxim.butenko.weather.util.HtmlHelper;
import maxim.butenko.weather.util.UrlPath;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@WebServlet(UrlPath.SIGN_IN)
public class SignInServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    private final SessionService sessionService = SessionService.getInstance();
    private final CookieHandler cookieHandler = CookieHandler.getInstance();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        Optional<UserDTO> optionalUserDTO = userService.findByLogin(login);

        if (optionalUserDTO.isPresent()) {
            UserDTO userDTO = optionalUserDTO.get();

            boolean isValid = BCrypt.verifyer().verify(password.toCharArray(), userDTO.getPassword()).verified;

            if (!isValid) {
                resp.sendRedirect(HtmlHelper.getHtmlPath(UrlPath.SIGN_IN));
                return;
            }

            Optional<WeatherSessionDTO> optionalWeatherSessionDTO = sessionService.add(userDTO);

            if (optionalWeatherSessionDTO.isPresent()) {

                WeatherSessionDTO sessionDTO = optionalWeatherSessionDTO.get();
                log.info("Session created: {}", sessionDTO.getId());

                Cookie cookie = cookieHandler.createSessionCookie(sessionDTO);
                resp.addCookie(cookie);
                log.info("Cookie created: {}", cookie.getValue());

                log.info("User successfully logged in: {}", login);
                resp.sendRedirect(HtmlHelper.getHtmlPath(UrlPath.AUTH_MAIN_PAGE));
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(HtmlHelper.getHtmlPath(UrlPath.SIGN_IN)).forward(req, resp);
    }
}
