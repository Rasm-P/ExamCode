/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Delivery;
import entities.Truck;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author rasmu
 */
public class TruckFacade {

    private static TruckFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private TruckFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static TruckFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TruckFacade();
        }
        return instance;
    }

    public List<Truck> getAllTrucks() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Truck> query
                    = em.createQuery("SELECT DISTINCT t FROM Truck t", Truck.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Truck createTruck(Truck truck) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(truck);
            em.getTransaction().commit();
            return truck;
        } finally {
            em.close();
        }
    }

    public Truck editTruck(Truck truck) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(truck);
            em.getTransaction().commit();
            return truck;
        } finally {
            em.close();
        }
    }

    public Truck removeTruck(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Truck truck = em.find(Truck.class, id);

            for (Delivery d : DeliveryFacade.getFacade(emf).getAllDeliveries()) {
                if (d.getTruck() != null && d.getTruck().getId().equals(truck.getId())) {
                    d.setTruck(null);
                    em.merge(d);
                }
            }

            em.remove(em.merge(truck));
            em.getTransaction().commit();
            return truck;
        } finally {
            em.close();
        }
    }

    public List<Truck> getBookedTrucksByDate(String date) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Truck> query
                    = em.createQuery("SELECT DISTINCT t FROM Truck t JOIN t.dileveryList l WHERE l.dateAsString = :date", Truck.class);
            query.setParameter("date", date);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

//    public List<Truck> getNonBookedTrucksByDate(String date) {
//        EntityManager em = emf.createEntityManager();
//        try {
//            TypedQuery<Truck> query
//                    = em.createQuery("SELECT DISTINCT t FROM Truck t JOIN t.dileveryList l WHERE l.dateAsString = :date", Truck.class);
//            query.setParameter("date", null);
//            return query.getResultList();
//        } finally {
//            em.close();
//        }
//    }
}
