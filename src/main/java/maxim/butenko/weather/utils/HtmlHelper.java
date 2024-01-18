package maxim.butenko.weather.utils;

public class HtmlHelper {

    private static final String HTML_FORMAT = "/view%s.html";

    public static String getHtmlPath(String view) {
        return String.format(HTML_FORMAT, view);
    }
}
