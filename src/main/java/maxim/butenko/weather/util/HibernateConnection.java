package maxim.butenko.weather.util;

import maxim.butenko.weather.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConnection {

    public static SessionFactory getConnection() {
        try {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(User.class);
            configuration.configure();

            return configuration.buildSessionFactory();

        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }
}
