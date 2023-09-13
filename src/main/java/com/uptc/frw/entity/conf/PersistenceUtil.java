package com.uptc.frw.entity.conf;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PersistenceUtil {
    private static EntityManagerFactory MY_ENTITY_MANAGER_FACTORY;


    public static EntityManager getEntityManagerOra(){
        if(MY_ENTITY_MANAGER_FACTORY==null){
            MY_ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("feriaUnitOra");
        }
        return MY_ENTITY_MANAGER_FACTORY.createEntityManager();
    }
}
