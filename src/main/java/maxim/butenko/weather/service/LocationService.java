package maxim.butenko.weather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import maxim.butenko.weather.dao.LocationDAO;
import maxim.butenko.weather.dao.UserDAO;
import maxim.butenko.weather.dto.LocationDTO;
import maxim.butenko.weather.dto.UserDTO;
import maxim.butenko.weather.entity.Location;
import maxim.butenko.weather.entity.User;
import maxim.butenko.weather.entity.locationDTOEntities.Weather;
import maxim.butenko.weather.util.CustomMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationService {

    private static final LocationService INSTANCE = new LocationService();

    private final LocationDAO locationDAO = LocationDAO.getInstance();

    private final CustomMapper customMapper = CustomMapper.getInstance();
    private ObjectMapper objectMapper   ;

    public static LocationService getInstance() {
        return INSTANCE;
    }
    private LocationService() {
    }

    public void add(LocationDTO locationDTO) {
        locationDAO.findByUser()
        Location location = customMapper.convertLocationDTOToLocation(locationDTO, users);
        locationDAO.add(location);
    }

    public void delete(LocationDTO locationDTO) {
        Location location = customMapper.convert(locationDTO, Location.class);
        locationDAO.delete(location);
    }

    public void update(LocationDTO locationDTO) {
        Location location = customMapper.convert(locationDTO, Location.class);
        locationDAO.update(location);
    }

    public List<LocationDTO> findByUser(User user) {
        List<Location> locations = locationDAO.findByUser(user);
        List<LocationDTO> locationDTOS = new ArrayList<>();
        for (Location location : locations) {
            LocationDTO locationDTO = customMapper.convert(location, LocationDTO.class);
            locationDTOS.add(locationDTO);
        }
        return locationDTOS;
    }

    public Optional<Location> findById(LocationDTO locationDTO) {
        Location location = customMapper.convert(locationDTO, Location.class);
        return locationDAO.findById(location.getId());
    }

    public Optional<Location> findByName(LocationDTO locationDTO) {
        Location location = customMapper.convert(locationDTO, Location.class);
        return locationDAO.findByName(location.getName());
    }

    public Optional<Location> findByCoordinates(LocationDTO locationDTO) {
        Location location = customMapper.convert(locationDTO, Location.class);
        return locationDAO.findByCoordinates(location.getLatitude(), location.getLongitude());
    }


}
