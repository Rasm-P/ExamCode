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
public class DeliveryFacade {

    private static DeliveryFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private DeliveryFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static DeliveryFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DeliveryFacade();
        }
        return instance;
    }

    public List<Delivery> getAllDeliveries() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Delivery> query
                    = em.createQuery("SELECT DISTINCT d FROM Delivery d", Delivery.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Delivery createDelivery(Delivery delivery) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(delivery);
            em.getTransaction().commit();
            return delivery;
        } finally {
            em.close();
        }
    }

    public Delivery editDelivery(Delivery delivery) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(delivery);
            em.getTransaction().commit();
            return delivery;
        } finally {
            em.close();
        }
    }

    public Delivery removeDelivery(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Delivery delivery = em.find(Delivery.class, id);

            for (Cargo c : CargoFacade.getFacade(emf).getAllCargo()) {
                if (c.getDilevery() != null && c.getDilevery().getId().equals(delivery.getId())) {
                    c.setDilevery(null);
                    em.merge(c);
                }
            }

            em.remove(em.merge(delivery));
            em.getTransaction().commit();
            return delivery;
        } finally {
            em.close();
        }
    }
}
