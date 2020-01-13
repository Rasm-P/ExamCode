/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Cargo;
import entities.Delivery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author rasmu
 */
public class CargoFacade {
            
    private static CargoFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private CargoFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static CargoFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CargoFacade();
        }
        return instance;
    }
    
    public List<Cargo> getAllCargo() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Cargo> query
                    = em.createQuery("SELECT c FROM Cargo c", Cargo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
