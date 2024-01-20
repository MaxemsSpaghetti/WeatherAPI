package maxim.butenko.weather.service;

import maxim.butenko.weather.dao.UserDAO;
import maxim.butenko.weather.dto.UserDTO;
import maxim.butenko.weather.entity.Role;
import maxim.butenko.weather.entity.User;
import maxim.butenko.weather.util.CustomMapper;

import java.util.Optional;

public class UserService {
    private static final UserService INSTANCE = new UserService();

    private final UserDAO userDAO = UserDAO.getInstance();

    private static final CustomMapper customMapper = CustomMapper.getInstance();

    private UserService() {

    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public Optional<UserDTO> findByLogin(String login) {
        Optional<User> user = userDAO.findByLogin(login);
        return Optional.ofNullable(user).map(user1 -> customMapper.convert(user1, UserDTO.class));
    }

    public Optional<UserDTO> addUser(String login, String password) {
        Optional<User> user = userDAO.add(buildUser(login, password));
        return Optional.ofNullable(user).map(user1 -> customMapper.convert(user1, UserDTO.class));
    }



    private User buildUser(String login, String password) {
        return User.builder()
                .login(login)
                .password(password)
                .role(Role.USER)
                .build();
    }
}
