/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Cargo;
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
                    = em.createQuery("SELECT DISTINCT c FROM Cargo c", Cargo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Cargo createCargo(Cargo cargo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cargo);
            em.getTransaction().commit();
            return cargo;
        } finally {
            em.close();
        }
    }

    public Cargo editCargo(Cargo cargo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cargo);
            em.getTransaction().commit();
            return cargo;
        } finally {
            em.close();
        }
    }

    public Cargo removeCargo(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Cargo cargo = em.find(Cargo.class, id);
            em.remove(em.merge(cargo));
            em.getTransaction().commit();
            return cargo;
        } finally {
            em.close();
        }
    }
}
