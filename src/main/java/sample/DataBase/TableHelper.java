package sample.DataBase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import sample.DataBase.Entities.BaseTable;
import sample.DataBase.Entities.BaseTable_;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.List;

public class TableHelper {
    private SessionFactory sessionFactory;

    public TableHelper(HibernateUtil hibernateUtil) {
        sessionFactory = hibernateUtil.getSessionFactory();
    }

    public List<BaseTable> addTableList (List<BaseTable> list){
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

   /*
    public long getInitialId (){
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(BaseTable.class);
        Root<BaseTable> root = cq.from(BaseTable.class);
        Selection selection = root.get(BaseTable_.timestamp);
        cq.select(cb.construct(BaseTable.class, selection)).where(cb.min(root.get(BaseTable_.timestamp)));
        Query query = session.createQuery(cq);
        long id = query.getFirstResult();
        session.close();
        return id;
    }

    public void writeToTxt (){
        long minId = getInitialId();
        Long maxId = minId +
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(BaseTable.class);
        Root<BaseTable> root = cq.from(BaseTable.class);
        Selection [] selections = {root.get(BaseTable_.timestamp),
                root.get(BaseTable_.stressThickness),
                root.get(BaseTable_.curvature),
                root.get(BaseTable_.distance)};
        ParameterExpression<Long> parameterIdStop = cb.parameter(Long.class, "idStop");
        cq.select(cb.construct(BaseTable.class, selections)).where(cb.min(root.get(BaseTable_.timestamp)));
        Query query = session.createQuery(cq);
        query.setParameter("length",6);

    }
    */

}
