package maxim.butenko.weather.service;

import maxim.butenko.weather.entity.Location;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {

    private static final String API_KEY = "7fc63dedf9199bd6d57dca29892fe248";

    private static final WeatherService INSTANCE = new WeatherService();

    private WeatherService() {

    }

    public static WeatherService getInstance() {
        return INSTANCE;
    }

    public String find(String input) throws IOException, InterruptedException {
        String location;
        if (input.startsWith("^\\d$")) {
            String[] coordinates = input.split(" ", 2);
            location = findByCoordinates(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1]));
        } else {
            location = findByName(input);
        }
        return location;
    }

    private String findByName(String city) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://api.openweathermap.org/data/2.5/weather?" +
                                "q=" + city
                                + "&units=metric" +
                                "&appid=" + API_KEY
                ))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private String findByCoordinates(Double latitude, Double longitude) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://api.openweathermap.org/data/2.5/weather?" +
                                "lat=" + latitude +
                                "&lon=" + longitude +
                                "&units=metric" +
                                "&appid=" + API_KEY
                ))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


}
