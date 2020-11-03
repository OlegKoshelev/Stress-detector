package sample.DataBase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.EntityType;
import sample.DataBase.Entities.*;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class TableHelper {
    private SessionFactory sessionFactory;

    public TableHelper(HibernateUtil hibernateUtil) {
        sessionFactory = hibernateUtil.getSessionFactory();
    }

    public List<BaseTable> addTableList(List<BaseTable> list) {
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

    public BaseTable getTableType() {
        if (this instanceof DetailedTableHelper)
            return new DetailedTable();
        if (this instanceof RegularTableHelper)
            return new RegularTable();
        if (this instanceof AbbreviatedTableHelper)
            return new AbbreviatedTable();

        return null;
    }

    public CriteriaQuery getCriteriaQuery(CriteriaBuilder cb) {
        if (this instanceof DetailedTableHelper)
            return cb.createQuery(DetailedTable.class);
        if (this instanceof RegularTableHelper)
            return cb.createQuery(RegularTable.class);
        if (this instanceof AbbreviatedTableHelper)
            return cb.createQuery(AbbreviatedTable.class);

        return null;
    }

    public Root<BaseTable> getRoot(CriteriaQuery cq) {
        if (this instanceof DetailedTableHelper)
            return cq.from(DetailedTable.class);
        if (this instanceof RegularTableHelper)
            return cq.from(RegularTable.class);
        if (this instanceof AbbreviatedTableHelper)
            return cq.from(AbbreviatedTable.class);

        return null;
    }

    public Class<? extends BaseTable> getTableClass() {
        if (this instanceof DetailedTableHelper)
            return DetailedTable.class;
        if (this instanceof RegularTableHelper)
            return RegularTable.class;
        if (this instanceof AbbreviatedTableHelper)
            return AbbreviatedTable.class;

        return null;
    }


    public long getInitialId() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(getTableClass());
        ;
        Root<BaseTable> root = cq.from(getTableClass());
        Selection[] selections = {root.get(BaseTable_.timestamp)};
        cb.construct(getTableType().getClass(), selections);
        cq.select(cb.least((root.get(BaseTable_.timestamp))));
        Query query = session.createQuery(cq);
        Long id = (Long) query.getSingleResult();
        session.close();
        return id;
    }

    public long getSecondId() {
        Long firstId = getInitialId();
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(getTableClass());
        ;
        Root<BaseTable> root = cq.from(getTableClass());
        Selection[] selections = {root.get(BaseTable_.timestamp)};
        cb.construct(getTableType().getClass(), selections);
        cq.select(cb.least((root.get(BaseTable_.timestamp)))).where(cb.notEqual(root.get(BaseTable_.timestamp), firstId));
        Query query = session.createQuery(cq);
        Long id = (Long) query.getSingleResult();
        session.close();
        return id;
    }

    public long getIncrement() {
        return getSecondId() - getInitialId();
    }

/*
    public List<BaseTable> getList() {
        int rowsCount = (int) getCount();
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(getTableClass());
        Root<BaseTable> root = cq.from(getTableClass());
        cb.asc(root.get(BaseTable_.timestamp));

        int first = 0;
        List<BaseTable> result = null;
        while (first < rowsCount) {
            int max = first +100;
            List<BaseTable> list = getRows(session,cq,first,max);
            String text = listToString(list);
            writeListToFile(text, "D:\\abc.txt");
            first = first+ 100;
        }
        session.close();

        return null ;
    }
 */

    public void tableToTxt(String path) {
        int rowsCount = (int) getCount();
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(getTableClass());
        Root<BaseTable> root = cq.from(getTableClass());
        cb.asc(root.get(BaseTable_.timestamp));

        int first = 0;
        List<BaseTable> result = null;
        while (first < rowsCount) {
            int max = first +1000;
            List<BaseTable> list = getRows(session,cq,first,max);
            String text = listToString(list);
            writeListToFile(text, path);
            first = first+ 1000;
        }
        System.out.println(first);
        session.close();
    }


    private List<BaseTable> getRows(Session session,CriteriaQuery cq ,int first, int max){
        Query query = session.createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(max);
        return  query.getResultList();
    }

    private String listToString (List <BaseTable> list){
        StringBuilder stringBuilder = new StringBuilder();
        for (BaseTable bt:list) {
            stringBuilder.append(bt.getTimestamp()).append(" ").
                    append(bt.getStressThickness()).append(" ").
                    append(bt.getCurvature()).append(" ").
                    append(bt.getDistance()).append("\n");
        }
        return stringBuilder.toString();
    }

    private void writeListToFile (String text, String path){
        try {
            Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public long getCount(){ // возвращает кол-во строк в таблице
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery <Long> cq = cb.createQuery(Long.class);
        Root<? extends BaseTable> root = cq.from(getTableClass());
        cq.select(cb.count(root));
        Query query = session.createQuery(cq);
        Long count = (Long) query.getSingleResult();
        session.close();
        return count;
    }

/*
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
