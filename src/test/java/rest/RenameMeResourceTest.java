package rest;

import entities.Cargo;
import entities.Delivery;
import entities.Driver;
import entities.Role;
import entities.Truck;
import entities.User;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//@Disabled
public class RenameMeResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.CREATE);

        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    User manager = new User("manager", "tessdt");
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

    private static String securityToken;

    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }
    
    @Test
    public void testGetAllHobbies() {
        given()
                .contentType("application/json")
                .get("/truck/allTrucks").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size", is(1));
    }

//    @Test
//    public void testCreateHobby() {
//        login("admin", "test");
//        given()
//                .contentType("application/json")
//                .accept(ContentType.JSON)
//                .header("x-access-token", securityToken)
//                .when()
//                .body("{\n"
//                        + "  \"name\": \"string\",\n"
//                        + "  \"description\": \"string\"\n"
//                        + "}")
//                .when()
//                .post("/hobby")
//                .then()
//                .statusCode(200);
//    }

//    @Test
//    public void testEditHobby() {
//        login("admin", "test");
//        given()
//                .contentType("application/json")
//                .header("x-access-token", securityToken)
//                .body("{\n"
//                        + "  \"id\": " + hobby.getId() + ",\n"
//                        + "  \"name\": \"newstring\",\n"
//                        + "  \"description\": \"string\"\n"
//                        + "}")
//                .when()
//                .put("/hobby")
//                .then()
//                .body("name", equalTo("newstring"))
//                .body("id", notNullValue());
//    }
//
//    @Test
//    public void testDeleteHobby() {
//        login("admin", "test");
//        given()
//                .contentType("application/json")
//                .accept(ContentType.JSON)
//                .header("x-access-token", securityToken)
//                .when()
//                .delete("/hobby/" + hobby.getId())
//                .then()
//                .statusCode(200);
//    }
}
