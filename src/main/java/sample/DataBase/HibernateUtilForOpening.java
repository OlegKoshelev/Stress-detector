package sample.DataBase;


import org.hibernate.cfg.Configuration;

public class HibernateUtilForOpening extends HibernateUtil{

    public HibernateUtilForOpening(String path) {
        super(new Configuration()
                .setProperty("hibernate.connection.url", "jdbc:sqlite:" + path)
                .setProperty("hibernate.hbm2ddl.auto", "validate")
                .configure());
    }
}
