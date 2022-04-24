package org.budtz;

import jakarta.persistence.Entity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import java.util.Set;

public class HiberSpeed {
    private SessionFactory sessionFactory;

    private boolean debug = false;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public enum Dialect {
        postgreSQL,
        mySQL
    }
    public enum SchemaGeneration {
        none, createonly, drop, create, createdrop,validate,update
    }

    /**
     *
     * @param dataPackage Package name for package containing Enities for mapping
     * @param userName Database username
     * @param passWord Database password
     * @param url url/IP for db. Format: ip|url{:port}{/defaultdb} - {optional}
     * By default connects to a PostgreSQL DB.
     */
    public HiberSpeed(String dataPackage, String userName, String passWord, String url){
        new HiberSpeed(dataPackage, userName,passWord, url, Dialect.postgreSQL, SchemaGeneration.none,false);
    }

    public HiberSpeed(String dataPackage, String userName, String passWord, String url, Dialect dialect, SchemaGeneration schemaGeneration, boolean debug) {
        this.debug=debug;
        Configuration configuration = new Configuration();
        Reflections reflections = new Reflections(dataPackage);
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
        entities.forEach((Class<?> c) -> {
            if (debug) System.out.println("Found: " + c);
            configuration.addAnnotatedClass(c);
        });

        configuration.setProperty("hibernate.connection.username",userName);
        configuration.setProperty("hibernate.connection.password",passWord);
        System.out.println("Database: " + dialect);
        switch (dialect) {
            case postgreSQL -> 
                configuration.setProperty("hibernate.connection.url","jdbc:postgresql://" + url );
            case mySQL ->
                configuration.setProperty("hibernate.connection.url","jdbc:mysql://" + url );
        }
        String hbm2dll = switch (schemaGeneration){
            case none -> "none";
            case createonly -> "create-only";
            case drop -> "drop";
            case create -> "create";
            case createdrop -> "create-drop";
            case validate -> "validate";
            case update -> "update";
        };
        System.out.println("SchemaGeneration: " + hbm2dll);

        configuration.setProperty("hibernate.hbm2ddl.auto",hbm2dll);
        this.sessionFactory = configuration.buildSessionFactory();

    }

}
