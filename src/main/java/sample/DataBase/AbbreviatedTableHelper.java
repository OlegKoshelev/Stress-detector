package sample.DataBase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import sample.DataBase.Entities.AbbreviatedTable;

import java.util.List;

public class AbbreviatedTableHelper {

    private SessionFactory sessionFactory;

    public AbbreviatedTableHelper() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<AbbreviatedTable> addAbbreviatedTableList (List<AbbreviatedTable> list){
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
