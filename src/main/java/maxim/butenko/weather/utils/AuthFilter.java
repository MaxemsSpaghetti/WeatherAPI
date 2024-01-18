package maxim.butenko.weather.utils;

import maxim.butenko.weather.dto.UserDTO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

import static java.util.Objects.nonNull;
import static maxim.butenko.weather.utils.UrlPath.LOGIN;
import static maxim.butenko.weather.utils.UrlPath.REGISTRATION;

@WebFilter("/*")
public class AuthFilter implements Filter {

    public static final Set<String> EXCLUDED_URLS = Set.of(LOGIN, REGISTRATION);

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String uri = req.getRequestURI();

//        HttpSession session = req.getSession();
//        String loginURI = req.getContextPath() + "/login";
//
//        boolean isLoginPage = req.getRequestURI().equals(loginURI);
//        boolean isAuthenticationPage = req.getRequestURI().contains("/authentication");
//        boolean isLoggedIn = nonNull(session) &&
//                nonNull(session.getAttribute("login")) &&
//                nonNull(session.getAttribute("password"));

        if (isUserLoggedIn(req) || isExcludedUrl(uri)) {
                filterChain.doFilter(req, resp);
        } else {
            resp.sendRedirect(LOGIN);
        }
    }

    private boolean isExcludedUrl(String uri) {
        return EXCLUDED_URLS.stream().anyMatch(uri::startsWith);
    }

    private boolean isUserLoggedIn(HttpServletRequest req) {
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        return nonNull(user);
    }
}
