/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Truck;
import java.util.List;
import javax.persistence.EntityManagerFactory;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
