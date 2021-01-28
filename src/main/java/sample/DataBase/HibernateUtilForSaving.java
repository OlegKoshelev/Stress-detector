package sample.DataBase;


import org.hibernate.cfg.Configuration;

public class HibernateUtilForSaving extends HibernateUtil {
    public HibernateUtilForSaving(String path) {
        super(new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect")
                .setProperty("hibernate.connection.url", "jdbc:sqlite:" + path)
                .setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC")
                .setProperty("hibernate.current_session_context_class", "thread")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .configure());
    }
}
