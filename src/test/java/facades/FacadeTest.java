package facades;

import entities.Cargo;
import entities.Delivery;
import entities.Driver;
import entities.Role;
import entities.Truck;
import entities.User;
import errorhandling.AuthenticationException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//@Disabled
public class FacadeTest {

    private static EntityManagerFactory emf;
    private static CargoFacade cargoFacade;
    private static DeliveryFacade deliveryFacade;
    private static DriverFacade driverFacade;
    private static TruckFacade truckFacade;
    private static UserFacade userFacade;

    public FacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        cargoFacade = CargoFacade.getFacade(emf);
        deliveryFacade = DeliveryFacade.getFacade(emf);
        driverFacade = DriverFacade.getFacade(emf);
        truckFacade = TruckFacade.getFacade(emf);
        userFacade = UserFacade.getUserFacade(emf);
    }

    User manager = new User("manager", "test");
    Role managerRole = new Role("manager");

    Date date = new Date();
    Delivery delivery = new Delivery(date, "Aldershvilevej 2", "Byvej 64");
    Cargo cargo = new Cargo("Phones", 56.03, 64);
    Truck truck = new Truck("Gamle Ole", 6000);
    Driver driver = new Driver("Ole Olsen");

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            em.createQuery("delete from Cargo").executeUpdate();
            em.createQuery("delete from Delivery").executeUpdate();
            em.createQuery("delete from Truck").executeUpdate();
            em.createQuery("delete from Driver").executeUpdate();

            manager.addRole(managerRole);
            em.persist(managerRole);
            em.persist(manager);

            truck.setDriver(driver);
            delivery.setTruck(truck);
            cargo.setDilevery(delivery);

            em.persist(driver);
            em.persist(truck);
            em.persist(cargo);
            em.persist(delivery);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetAllCargo() {
        List<Cargo> cargoList = cargoFacade.getAllCargo();
        assertEquals(cargoList.size(), 1);
    }

    @Test
    public void testCreateCargo() {
        Cargo c1 = new Cargo("test", 33.33, 33);
        Cargo c2 = cargoFacade.createCargo(c1);
        assertEquals(c1.getId(), c2.getId());
    }

    @Test
    public void testEditCargo() {
        cargo.setUnits(100);
        Cargo c = cargoFacade.editCargo(cargo);
        assertEquals(c.getUnits(), 100);
    }

    @Test
    public void testRemoveCargo() {
        Cargo c = cargoFacade.removeCargo(cargo.getId());
        EntityManager em = emf.createEntityManager();
        Cargo c2;
        try {
            c2 = em.find(Cargo.class, c.getId());
        } finally {
            em.close();
        }
        assertEquals(cargoFacade.getAllCargo().size(), 0);
        assertEquals(c2, null);
    }

    @Test
    public void testGetAllDelivery() {
        List<Delivery> deliveryList = deliveryFacade.getAllDeliveries();
        assertEquals(deliveryList.size(), 1);
    }

    @Test
    public void testCreateDelivery() {
        Delivery d1 = new Delivery(date, "testVej1", "testVej2");
        Delivery d2 = deliveryFacade.createDelivery(d1);
        assertEquals(d1.getId(), d2.getId());
    }

    @Test
    public void testEditDelivery() {
        delivery.setFromLocation("testVej3");
        Delivery d = deliveryFacade.editDelivery(delivery);
        assertEquals(d.getFromLocation(), "testVej3");
    }

    @Test
    public void testRemoveDelivery() {
        Delivery d = deliveryFacade.removeDelivery(delivery.getId());
        EntityManager em = emf.createEntityManager();
        Delivery d2;
        try {
            d2 = em.find(Delivery.class, d.getId());
        } finally {
            em.close();
        }
        assertEquals(deliveryFacade.getAllDeliveries().size(), 0);
        assertEquals(d2, null);
    }

    @Test
    public void testGetAllDriver() {
        List<Driver> driverList = driverFacade.getAllDrivers();
        assertEquals(driverList.size(), 1);
    }

    @Test
    public void testCreateDriver() {
        Driver d1 = new Driver("Test");
        Driver d2 = driverFacade.createDriver(d1);
        assertEquals(d1.getId(), d2.getId());
    }

    @Test
    public void testEditDriver() {
        driver.setName("newTest");
        Driver d = driverFacade.editDriver(driver);
        assertEquals(d.getName(), "newTest");
    }

    @Test
    public void testRemoveDriver() {
        Driver d = driverFacade.removeDriver(driver.getId());
        EntityManager em = emf.createEntityManager();
        Driver d2;
        try {
            d2 = em.find(Driver.class, d.getId());
        } finally {
            em.close();
        }
        assertEquals(driverFacade.getAllDrivers().size(), 0);
        assertEquals(d2, null);
    }

    @Test
    public void testGetDriversByDate() {
        List<Driver> driverList = driverFacade.getBookedDriversByDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
        assertEquals(driverList.size(), 1);
    }

    @Test
    public void testGetAllTruck() {
        List<Truck> truckList = truckFacade.getAllTrucks();
        assertEquals(truckList.size(), 1);
    }

    @Test
    public void testCreateTruck() {
        Truck t1 = new Truck("Test", 6000);
        Truck t2 = truckFacade.createTruck(t1);
        assertEquals(t1.getId(), t2.getId());
    }

    @Test
    public void testEditTruck() {
        truck.setName("newTest");
        Truck t = truckFacade.editTruck(truck);
        assertEquals(t.getName(), "newTest");
    }

    @Test
    public void testRemoveTruck() {
        Truck t = truckFacade.removeTruck(truck.getId());
        EntityManager em = emf.createEntityManager();
        Truck t2;
        try {
            t2 = em.find(Truck.class, t.getId());
        } finally {
            em.close();
        }
        assertEquals(truckFacade.getAllTrucks().size(), 0);
        assertEquals(t2, null);
    }

    @Test
    public void testGetTrucksByDate() {
        List<Truck> driverList = truckFacade.getBookedTrucksByDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
        assertEquals(driverList.size(), 1);
    }

    @Test
    public void testGetVeryfiedUser() throws AuthenticationException {
        User u = userFacade.getVeryfiedUser(manager.getUserName(), "test");
        assertEquals(u.getUserName(), manager.getUserName());
    }

}
