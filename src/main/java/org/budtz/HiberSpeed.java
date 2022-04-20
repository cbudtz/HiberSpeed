package org.budtz;

import jakarta.persistence.Entity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import java.util.Set;

public class HiberSpeed {
    private SessionFactory sessionFactory;
    private boolean debug = false;

    public enum dialect{
        postgreSQL95
    }

    public HiberSpeed(String dataPackage, String userName, String passWord, String url){
        new HiberSpeed(dataPackage, userName,passWord, url, dialect.postgreSQL95);
    }

    public HiberSpeed(String dataPackage, String userName, String passWord, String url, dialect dialect) {
        Configuration configuration = new Configuration();
        Reflections reflections = new Reflections(dataPackage);
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
        entities.forEach((Class<?> c) -> {
            if (debug) System.out.println("Found: " + c);
            configuration.addAnnotatedClass(c);
        });
        configuration.setProperty("hibernate.dialect","org.hibernate.dialect.PostgreSQL95Dialect");
        configuration.setProperty("hibernate.connection.username","postgres");
        configuration.setProperty("hibernate.connection.password",passWord);
        configuration.setProperty("hibernate.connection.url","jdbc:postgresql://localhost:5432/hibernatedb");
        configuration.setProperty("hibernate.hbm2ddl.auto","update");
        this.sessionFactory = configuration.buildSessionFactory();

    }

}
