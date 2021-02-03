package sample.DataBase;


import org.hibernate.cfg.Configuration;

public class HibernateUtilForSaving extends HibernateUtil {
    public HibernateUtilForSaving(String path) {
        super(new Configuration()
                .setProperty("hibernate.connection.url", "jdbc:sqlite:" + path)
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .configure());
    }
}
