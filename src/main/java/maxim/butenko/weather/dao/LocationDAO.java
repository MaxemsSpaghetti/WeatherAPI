package maxim.butenko.weather.dao;

import lombok.extern.slf4j.Slf4j;
import maxim.butenko.weather.entity.Location;
import maxim.butenko.weather.entity.User;
import maxim.butenko.weather.util.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
@Slf4j
public class LocationDAO {
    private static final LocationDAO INSTANCE = new LocationDAO();

    private final SessionFactory sessionFactory = HibernateConnection.getConnection();

    public static LocationDAO getInstance() {
        return INSTANCE;
    }

    private LocationDAO() {
    }

    public void add(Location location) {
        Session hibernateSession = sessionFactory.getCurrentSession();
        try {

            hibernateSession.beginTransaction();
            hibernateSession.persist(location);
            hibernateSession.getTransaction().commit();

        } catch (Exception e) {
            log.error("An error occurred while adding location: {}", e.getMessage(), e);
            hibernateSession.getTransaction().rollback();
        }
    }

    public void delete(Location location) {
        Session hibernateSession = sessionFactory.getCurrentSession();
        try {

            hibernateSession.beginTransaction();
            hibernateSession.delete(location);
            hibernateSession.getTransaction().commit();

        } catch (Exception e) {
            log.error("An error occurred while deleting location: {}", e.getMessage(), e);
            hibernateSession.getTransaction().rollback();
        }
    }

    public void update(Location location) {
        Session hibernateSession = sessionFactory.getCurrentSession();
        try {

            hibernateSession.beginTransaction();
            hibernateSession.update(location);
            hibernateSession.getTransaction().commit();
        } catch (Exception e) {
            log.error("An error occurred while updating location: {}", e.getMessage(), e);
            hibernateSession.getTransaction().rollback();
        }
    }

    public List<Location> findByUser(User user) {
        Session hibernateSession = sessionFactory.getCurrentSession();
        try {
            hibernateSession.beginTransaction();
            Query<Location> query = hibernateSession.createQuery("SELECT l FROM Location l " +
                    "INNER JOIN l.users u " +
                    "WHERE u.id = :user_id", Location.class);
            query.setParameter("user_id", user.getId());
            List<Location> resultList = query.getResultList();
            hibernateSession.getTransaction().commit();

            return resultList;
        } catch (Exception e) {
            log.error("An error occurred while finding location by user: {}", e.getMessage(), e);
            hibernateSession.getTransaction().rollback();
        }
        return List.of();
    }

    public Optional<Location> findById(BigInteger id) {
        Session hibernateSession = sessionFactory.getCurrentSession();

        try {

            hibernateSession.beginTransaction();
            Location location = hibernateSession.get(Location.class, id);
            hibernateSession.getTransaction().commit();

            return Optional.of(location);

        } catch (Exception e) {
            log.error("An error occurred while finding location by id:{}", e.getMessage(), e);
            hibernateSession.getTransaction().rollback();
        }
        return Optional.empty();
    }

    public Optional<Location> findByName(String name) {
        Session hibernateSession = sessionFactory.getCurrentSession();
        try {
            hibernateSession.beginTransaction();
            Location location = hibernateSession.get(Location.class, name);
            hibernateSession.getTransaction().commit();

            return Optional.of(location);
        } catch (Exception e) {
            log.error("An error occurred while finding location by name: {}", e.getMessage(), e);
            hibernateSession.getTransaction().rollback();
        }
        return Optional.empty();
    }

    public Optional<Location> findByCoordinates(Double latitude , Double longitude) {
        Session hibernateSession = sessionFactory.getCurrentSession();
        try {
            hibernateSession.beginTransaction();
            Query<Location> query = hibernateSession.createQuery("SELECT l FROM Location l " +
                    "WHERE l.latitude =:latitude" +
                    " AND l.longitude =:longitude", Location.class);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            Location location = query.uniqueResult();

            hibernateSession.getTransaction().commit();

            return Optional.of(location);
        } catch (Exception e) {
            log.error("An error occurred while finding location by coordinates: {}", e.getMessage(), e);
            hibernateSession.getTransaction().rollback();
        }
        return Optional.empty();
    }
}
