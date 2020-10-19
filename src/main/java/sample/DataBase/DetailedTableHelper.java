package sample.DataBase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import sample.DataBase.Entities.DetailedTable;

import java.util.List;

public class DetailedTableHelper {
    private SessionFactory sessionFactory;

    public DetailedTableHelper() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<DetailedTable> addDetailedTableList (List<DetailedTable> list){
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
