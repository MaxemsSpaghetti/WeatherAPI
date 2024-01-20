package maxim.butenko.weather.dao;

import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.entity.WeatherSession;
import maxim.butenko.weather.util.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;
import java.util.UUID;


@Slf4j
public class SessionDAO {

    private final SessionFactory sessionFactory = HibernateConnection.getConnection();

    private static final SessionDAO INSTANCE = new SessionDAO();
    public static SessionDAO getInstance() {
        return INSTANCE;
    }
    private SessionDAO() {

    }

    public Optional<WeatherSession> add(WeatherSession session) {
        Session hibernateSession = sessionFactory.getCurrentSession();

        try {
            hibernateSession.beginTransaction();
            hibernateSession.persist(session);
            hibernateSession.getTransaction().commit();

            return Optional.of(session);
        } catch (Exception e) {
            log.error("An error occurred while adding session: {}", e.getMessage(), e);
            hibernateSession.getTransaction().rollback();
        }

        return Optional.empty();
    }

    public Optional<WeatherSession> findById(UUID id) {
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();
            WeatherSession weatherSession = session.get(WeatherSession.class, id);
            session.getTransaction().commit();

            return Optional.of(weatherSession);

        } catch (Exception e) {
            log.error("An error occurred while finding session by id: {}", e.getMessage(), e);
            session.getTransaction().rollback();
        }

        return Optional.empty();
    }

    public void delete(UUID id) {
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();
            WeatherSession weatherSession = session.get(WeatherSession.class, id);
            session.delete(weatherSession);

            session.getTransaction().commit();

        } catch (Exception e) {
            log.error("An error occurred while deleting session: {}", e.getMessage(), e);
            session.getTransaction().rollback();
        }
    }
}
