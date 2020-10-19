package sample.DataBase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import sample.DataBase.Entities.RegularTable;

import java.util.List;

public class RegularTableHelper {

    private SessionFactory sessionFactory;

    public RegularTableHelper() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<RegularTable> addRegularTableList(List<RegularTable> list) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            session.save(list.get(i));
            if (i % 10 == 0) {
                session.flush();
            }
        }
        session.getTransaction().commit();
        session.close();
        return list;
    }

}
