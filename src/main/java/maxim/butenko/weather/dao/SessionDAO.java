package maxim.butenko.weather.dao;

import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.entity.WeatherSession;
import maxim.butenko.weather.util.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
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
        Session hibernateSession = sessionFactory.getCurrentSession();

        try {
            hibernateSession.beginTransaction();
            WeatherSession weatherSession = hibernateSession.get(WeatherSession.class, id);
            hibernateSession.getTransaction().commit();

            return Optional.of(weatherSession);

        } catch (Exception e) {
            log.error("An error occurred while finding session by id: {}", e.getMessage(), e);
            hibernateSession.getTransaction().rollback();
        }

        return Optional.empty();
    }

    public void delete(UUID id) {
        Session hibernateSession = sessionFactory.getCurrentSession();

        try {
            hibernateSession.beginTransaction();
            WeatherSession weatherSession = hibernateSession.get(WeatherSession.class, id);
            hibernateSession.delete(weatherSession);

            hibernateSession.getTransaction().commit();

        } catch (Exception e) {
            log.error("An error occurred while deleting session by id: {}", e.getMessage(), e);
            hibernateSession.getTransaction().rollback();
        }
    }

    public void deleteByExpiredTime(LocalDateTime time) {
        Session hibernateSession = sessionFactory.getCurrentSession();

        try {
            hibernateSession.beginTransaction();

            Query query = hibernateSession.createQuery("DELETE FROM WeatherSession s WHERE s.expiresAt <= :time");
            query.setParameter("time", time);
            query.executeUpdate();

            hibernateSession.getTransaction().commit();
        } catch (Exception e) {
            log.error("An error occurred while deleting session by expired time: {}", e.getMessage(), e);
            hibernateSession.getTransaction().rollback();
        }
    }
}
