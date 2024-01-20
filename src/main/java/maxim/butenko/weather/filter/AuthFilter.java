package maxim.butenko.weather.filter;

import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.dto.UserDTO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

import static java.util.Objects.nonNull;
import static maxim.butenko.weather.util.UrlPath.LOGIN;
import static maxim.butenko.weather.util.UrlPath.REGISTRATION;
@Slf4j
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
        log.info("Request URI: {}", uri);

        if (isUserLoggedIn(req) || isExcludedUrl(uri)) {
            log.info("Allowing access to: {}", uri);
                filterChain.doFilter(req, resp);
        } else {
            log.info("Redirecting to previous page from URI: {}", uri);
            reject(req, resp);
        }
    }

    private boolean isExcludedUrl(String uri) {
        return EXCLUDED_URLS.stream().anyMatch(uri::contains);
    }

    private boolean isUserLoggedIn(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void reject(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var prevPage = req.getHeader("referer");
        resp.sendRedirect(prevPage != null ? prevPage : LOGIN);
    }
}
