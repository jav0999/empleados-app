package com.ez.sisemp.empleado.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class JpaConfig {
    private JpaConfig(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devUnit");
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }


}
