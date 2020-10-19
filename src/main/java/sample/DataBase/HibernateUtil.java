package sample.DataBase;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private String path;
    private Configuration dBConfiguration;

    public HibernateUtil(String path) {
        this.path = path;
        String url = "jdbc:sqlite:" + path;
        dBConfiguration = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect")
                .setProperty("hibernate.connection.url", url)
                .setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC")
                .setProperty("hibernate.current_session_context_class", "thread")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .configure();
        sessionFactory = dBConfiguration.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
