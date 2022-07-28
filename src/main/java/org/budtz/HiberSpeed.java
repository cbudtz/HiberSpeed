package org.budtz;

import jakarta.persistence.Entity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import java.util.Set;

public class HiberSpeed {
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public Session openSession(){
        return sessionFactory.openSession();
    }

    public enum Dialect {
        postgreSQL,
        mySQL
    }
    public enum SchemaGeneration {
        /**
         * Makes no changes to schema in the database
         */
        none,
        /**
         * Creates Schema, preserving existing data.
         */
        createonly,
        /**
         * Drops Schema matching entities.
         */
        drop,
        /**
         * Creates Schema, <b>destroys</b> previous data.
         */
        create,
        /**
         * Creates Schema, <b>destroys</b> previous data. Also drops Schema on sessionFactory close.
         */
        createdrop,
        /**
         * Validates that Schema matches entities.
         */
        validate,
        /**
         * Updates existing Schema, preserving data.
         */
        update
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
        Configuration configuration = new Configuration();
        Reflections reflections = new Reflections(dataPackage);
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
        entities.forEach((Class<?> c) -> {
            if (debug) {
                System.out.println("Found: " + c);
                configuration.setProperty("hibernate.show_sql", "true");
                configuration.setProperty("log4j.logger.org.hibernate", "info");
            }
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
