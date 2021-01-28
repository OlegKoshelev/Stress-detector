package sample.DataBase;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;


    public  HibernateUtil( Configuration configuration) {

        sessionFactory = configuration.buildSessionFactory();

    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
