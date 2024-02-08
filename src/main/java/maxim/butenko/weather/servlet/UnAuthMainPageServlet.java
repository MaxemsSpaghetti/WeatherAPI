package maxim.butenko.weather.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.dto.LocationDTO;
import maxim.butenko.weather.service.WeatherService;
import maxim.butenko.weather.util.HtmlHelper;
import maxim.butenko.weather.util.UrlPath;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(UrlPath.UN_AUTH_MAIN_PAGE)
public class UnAuthMainPageServlet extends HttpServlet {

   private final WeatherService weatherService = WeatherService.getInstance();

   private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String input = req.getParameter("input");
        try {
            String location = weatherService.find(input);
            LocationDTO locationDTO = objectMapper.readValue(location, LocationDTO.class);
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(locationDTO);
            resp.getWriter().write(json);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(HtmlHelper.getHtmlPath(UrlPath.UN_AUTH_MAIN_PAGE)).forward(req, resp);
    }
}
