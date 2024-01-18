package maxim.butenko.weather.service;

import maxim.butenko.weather.dao.UserDAOImpl;
import maxim.butenko.weather.dto.UserDTO;
import maxim.butenko.weather.entity.Role;
import maxim.butenko.weather.entity.User;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class UserService {
    private static final UserService INSTANCE = new UserService();

    private final UserDAOImpl userDAO = UserDAOImpl.getInstance();

    private static final ModelMapper modelMapper = new ModelMapper();

    private UserService() {

    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public Optional<UserDTO> findByLoginAndPassword(String login, String password) {
        Optional<User> user = userDAO.findByLoginAndPassword(login, password);

        return Optional.ofNullable(user).map(user1 -> modelMapper.map(user1, UserDTO.class));
    }

    public Optional<UserDTO> addUser(String login, String password) {
        User user = buildUser(login, password);
        userDAO.add(user);
        return Optional.ofNullable(user).map(user1 -> modelMapper.map(user1, UserDTO.class));
    }



    private User buildUser(String login, String password) {
        return User.builder()
                .login(login)
                .password(password)
                .role(Role.USER)
                .build();
    }
}
