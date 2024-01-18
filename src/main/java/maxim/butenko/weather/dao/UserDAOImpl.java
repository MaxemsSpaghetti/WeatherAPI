package maxim.butenko.weather.dao;

import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.entity.User;
import maxim.butenko.weather.utils.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Objects;
import java.util.Optional;
@Slf4j
public class UserDAOImpl {

    private static final UserDAOImpl INSTANCE = new UserDAOImpl();

    private final SessionFactory sessionFactory = HibernateConnection.getConnection();

    private UserDAOImpl() {

    }

    public static UserDAOImpl getInstance() {
        return INSTANCE;
    }

    public void add(User user) {
        Session session = sessionFactory.getCurrentSession();
        try (session) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("An error occurred while adding user: {}", e.getMessage(), e);
            log.error("Stack trace: ", e);
            session.getTransaction().rollback();
        }
    }
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Session session = sessionFactory.getCurrentSession();
        try (session) {
            session.beginTransaction();

            Query<User> query = session.createQuery("from User where login = :login and password = :password");

            query.setParameter("login", login);
            query.setParameter("password", password);
            session.getTransaction().commit();
            User user = query.uniqueResult();

            if (user != null && Objects.equals(user.getPassword(), password)) {
                return Optional.of(user);
            }

        } catch (Exception e) {
            log.error("An error occurred while processing login and password: {}", e.getMessage(), e);
            log.error("Stack trace: ", e);
            session.getTransaction().rollback();
        }

        return Optional.empty();
    }
}
