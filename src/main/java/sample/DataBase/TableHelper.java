package sample.DataBase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.EntityType;
import sample.AdditionalUtils.TableHelperUtils;
import sample.DataBase.Entities.*;
import sample.Graph.BoundaryValues;
import sample.InitialDataSetting.Graph.GraphType;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


    public Class<? extends BaseTable> getTableClass() {
        if (this instanceof DetailedTableHelper)
            return DetailedTable.class;
        if (this instanceof AbbreviatedTableHelper)
            return AveragingTable.class;

        return null;
    }

    public void tableToTxt(String path) {
        int rowsCount = (int) getCount();
        String columnNames = "Time (long)   Stress*Thickness(GPa*um)   Curvature(m^[-1])   Distance(pixels) \r\n";
        writeListToFile(columnNames, path);
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(getTableClass());
        Root<BaseTable> root = cq.from(getTableClass());
        cb.asc(root.get(BaseTable_.timestamp));

        for (int first = 0; first < rowsCount; first = first + 100) {
            List<BaseTable> list = null;
            int max = 100;
            list = getRows(session, cq, first, max);
            String text = listToString(list);
            writeListToFile(text, path);
        }
        session.close();
    }


    private List<BaseTable> getRows(Session session, CriteriaQuery cq, int first, int max) {
        Query query = session.createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(max);
        return query.getResultList();
    }



    private String listToString(List<BaseTable> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (BaseTable bt : list) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SS");
            stringBuilder.append(dateFormat.format(new Date(bt.getTimestamp()))).append("   ").
                    append(bt.getStressThickness()).append("   ").
                    append(bt.getCurvature()).append("   ").
                    append(bt.getDistance()).append("\r\n");
        }
        return stringBuilder.toString();
    }

    private void writeListToFile(String text, String path) {
        try {
            Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<BaseTable> getTable() {
        List<BaseTable> result = new ArrayList<>();
        int rowsCount = (int) getCount();
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(getTableClass());
        Root<BaseTable> root = cq.from(getTableClass());
        cb.asc(root.get(BaseTable_.timestamp));
        for (int first = 0; first < rowsCount; first = first + 100) {
            List<BaseTable> list = null;
            int max = 100;
            list = getRows(session, cq, first, max);
            result.addAll(list);
        }
        session.close();
        return result;
    }

    public long getCount() { // возвращает кол-во строк в таблице
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<? extends BaseTable> root = cq.from(getTableClass());
        cq.select(cb.count(root));
        Query query = session.createQuery(cq);
        Long count = (Long) query.getSingleResult();
        session.close();
        return count;
    }


}
