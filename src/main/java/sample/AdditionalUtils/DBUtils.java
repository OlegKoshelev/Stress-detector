package sample.AdditionalUtils;

import org.hibernate.Session;
import sample.DataBase.Entities.BaseTable;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DBUtils {
    public static  List<? extends BaseTable> getRows(Session session, CriteriaQuery<? extends BaseTable> criteriaQuery, int first, int max) {
        Query query = session.createQuery(criteriaQuery);
        query.setFirstResult(first);
        query.setMaxResults(max);
        return query.getResultList();
    }

    public static String listToString(List<? extends BaseTable> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (BaseTable bt : list) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SS");
            stringBuilder.append(dateFormat.format(new Date(bt.getTimestamp()))).append("   ").
                    append(bt.getStressThickness()).append("   ").
                    append(bt.getCurvature()).append("   ").
                    append(bt.getDistance()).append("   ").
                    append(bt.getX1()).append("   ").
                    append(bt.getY1()).append("   ").
                    append(bt.getX2()).append("   ").
                    append(bt.getY2()).append("   ").
                    append(bt.getSize1()).append("   ").
                    append(bt.getSize2()).append("\r\n");
        }
        return stringBuilder.toString();
    }

    public static void writeListToFile(String text, String path) {
        try {
            Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
