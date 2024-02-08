package maxim.butenko.weather.filter;

import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.util.CookieHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static maxim.butenko.weather.util.UrlPath.SIGN_IN;
import static maxim.butenko.weather.util.UrlPath.SIGN_UP;
@Slf4j
@WebFilter("/*")
public class AuthFilter implements Filter {

    public static final Set<String> EXCLUDED_URLS = Set.of(SIGN_IN, SIGN_UP);

    public static final CookieHandler cookieHandler = CookieHandler.getInstance();

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
        return cookieHandler.getSessionCookie(req).isPresent();
    }


    private void reject(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String prevPage = req.getHeader("referer");
        resp.sendRedirect(prevPage != null ? prevPage : SIGN_IN);
    }
}
