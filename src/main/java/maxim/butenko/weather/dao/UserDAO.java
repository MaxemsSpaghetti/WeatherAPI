package maxim.butenko.weather.dao;

import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.entity.Role;
import maxim.butenko.weather.entity.User;
import maxim.butenko.weather.util.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Optional;
@Slf4j
public class UserDAO {

    private static final UserDAO INSTANCE = new UserDAO();

    private final SessionFactory sessionFactory = HibernateConnection.getConnection();

    private UserDAO() {

    }

    public static UserDAO getInstance() {
        return INSTANCE;
    }

    public Optional<User> add(User user) {
        Session hibernateSession = sessionFactory.getCurrentSession();
        try {

            hibernateSession.beginTransaction();
            hibernateSession.persist(user);
            hibernateSession.getTransaction().commit();

            return Optional.of(user);

        } catch (Exception e) {
            log.error("An error occurred while adding user: {}", e.getMessage(), e);
            log.error("Stack trace: ", e);
            hibernateSession.getTransaction().rollback();
        }
        return Optional.empty();
    }
    public Optional<User> findByLogin(String login) {
        Session hibernateSession = sessionFactory.getCurrentSession();
        try {
            hibernateSession.beginTransaction();

            Query<User> query = hibernateSession.createQuery("SELECT u FROM User u WHERE login = :login", User.class);

            query.setParameter("login", login);
            User user = query.uniqueResult();

            hibernateSession.getTransaction().commit();

            user.setRole(Role.USER);
            return Optional.of(user);

        } catch (Exception e) {
            log.error("An error occurred while processing login and password: {}", e.getMessage(), e);
            log.error("Stack trace: ", e);
            hibernateSession.getTransaction().rollback();
        }

        return Optional.empty();
    }
}
