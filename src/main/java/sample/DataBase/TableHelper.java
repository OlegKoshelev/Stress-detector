package sample.DataBase;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import sample.AdditionalUtils.DBUtils;
import sample.DataBase.Entities.*;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class TableHelper<Type extends BaseTable>{
    private SessionFactory sessionFactory;

    private  Class<Type> type;

    private final Logger logger = Logger.getLogger(TableHelper.class);

    public TableHelper(HibernateUtil hibernateUtil,Class<Type> type) {
        sessionFactory = hibernateUtil.getSessionFactory();
        this.type = type;
    }



    public List<Type> addTableList(List<Type> list) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            session.save(list.get(i));
            if (i % 100 == 0) {
                session.flush();
                logger.debug("flush");
            }
        }
        session.getTransaction().commit();
        session.close();
        return list;
    }

    public void tableToTxt(String path) {
        int rowsCount = (int) getCount();
        String columnNames = "Time (long)   Stress*Thickness(GPa*um)   Curvature(m^[-1])   Distance(pixels) \r\n";
        DBUtils.writeListToFile(columnNames, path);
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Type> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<Type> root = criteriaQuery.from(type);
        criteriaBuilder.asc(root.get(BaseTable_.timestamp));

        for (int first = 0; first < rowsCount; first = first + 100) {
            List<Type> list = null;
            int max = 100;
            list = (List<Type>) DBUtils.getRows(session, criteriaQuery, first, max);
            String text = DBUtils.listToString(list);
            DBUtils.writeListToFile(text, path);
        }
        session.close();
    }

    public List<Type> getTable() {
        List<Type> result = new ArrayList<>();
        int rowsCount = (int) getCount();
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Type> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<Type> root = criteriaQuery.from(type);
        criteriaBuilder.asc(root.get(BaseTable_.timestamp));
        for (int first = 0; first < rowsCount; first = first + 100) {
            List<Type> list = null;
            int max = 100;
            list = (List<Type>) DBUtils.getRows(session, criteriaQuery, first, max);
            result.addAll(list);
        }
        session.close();
        return result;
    }

    public long getCount() { // возвращает кол-во строк в таблице
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Type> root = cq.from(type);
        cq.select(cb.count(root));
        Query query = session.createQuery(cq);
        Long count = (Long) query.getSingleResult();
        session.close();
        return count;
    }


}
