package sample.DataBase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import sample.DataBase.Entities.InitialDistance;


public class InitialDistanceHelper {
    private SessionFactory sessionFactory;


    public InitialDistanceHelper(HibernateUtil hibernateUtil) {
        this.sessionFactory = hibernateUtil.getSessionFactory();
    }

    public void saveDistance(double d0) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        InitialDistance initialDistance = new InitialDistance(d0);
        session.save(initialDistance);
        session.getTransaction().commit();
        session.close();
    }

    public double getDistance() {
        Session session = sessionFactory.openSession();
        InitialDistance initialDistance = session.get(InitialDistance.class,1);
        session.close();
        if (initialDistance == null)
            return 0;
        return initialDistance.getD0();
    }
}
