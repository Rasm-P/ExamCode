/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Driver;
import entities.Truck;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author rasmu
 */
public class DriverFacade {

    private static DriverFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private DriverFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static DriverFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DriverFacade();
        }
        return instance;
    }

    public List<Driver> getAllDrivers() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Driver> query
                    = em.createQuery("SELECT d FROM Driver d", Driver.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Driver createDriver(Driver driver) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(driver);
            em.getTransaction().commit();
            return driver;
        } finally {
            em.close();
        }
    }

    public Driver editDriver(Driver driver) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(driver);
            em.getTransaction().commit();
            return driver;
        } finally {
            em.close();
        }
    }

    public Driver removeDriver(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Driver driver = em.find(Driver.class, id);

            for (Truck t : TruckFacade.getFacade(emf).getAllTrucks()) {
                if (t.getDriver().getId().equals(driver.getId())) {
                    t.setDriver(null);
                    em.merge(t);
                }
            }

            em.remove(em.merge(driver));
            em.getTransaction().commit();
            return driver;
        } finally {
            em.close();
        }
    }

    public List<Driver> getBookedDriversByDate(String date) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Driver> query
                    = em.createQuery("SELECT DISTINCT d FROM Driver d JOIN d.truck.dileveryList l WHERE l.dateAsString = :date", Driver.class);
            query.setParameter("date", date);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
