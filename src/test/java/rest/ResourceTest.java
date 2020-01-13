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
import org.junit.jupiter.api.Test;

//@Disabled
public class ResourceTest {

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
    public void testGetAllTrucks() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/truck/allTrucks").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size", is(1));
    }

    @Test
    public void testCreateTruck() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .body("{\n"
                        + "  \"name\": \"string\",\n"
                        + "  \"capacity\": 0,\n"
                        + "  \"driver\": null,\n"
                        + "  \"dileveryList\": []\n"
                        + "}")
                .when()
                .post("/truck")
                .then()
                .statusCode(200);
    }

    @Test
    public void testEditTruck() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body("{\n"
                        + "  \"id\": " + truck.getId() + ",\n"
                        + "  \"name\": \"newString\",\n"
                        + "  \"capacity\": 0,\n"
                        + "  \"driver\": null,\n"
                        + "  \"dileveryList\": []\n"
                        + "}")
                .when()
                .put("/truck")
                .then()
                .body("name", equalTo("newString"))
                .body("id", notNullValue());
    }

    @Test
    public void testDeleteTruck() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/truck/" + truck.getId())
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetAllDeliveries() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/delivery/allDeliveries").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size", is(1));
    }

    @Test
    public void testCreateDilevery() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .body("{\n"
                        + "  \"shippingDate\": \"2020-01-13T10:56:04.970Z\",\n"
                        + "  \"fromLocation\": \"string\",\n"
                        + "  \"toLocation\": \"string\",\n"
                        + "  \"cargoList\": [],\n"
                        + "  \"truck\": null \n"
                        + "}")
                .when()
                .post("/delivery")
                .then()
                .statusCode(200);
    }

    @Test
    public void testEditDilevery() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body("{\n"
                        + "  \"id\": " + delivery.getId() + ",\n"
                        + "  \"shippingDate\": \"2020-01-13T10:56:04.970Z\",\n"
                        + "  \"fromLocation\": \"newString\",\n"
                        + "  \"toLocation\": \"string\",\n"
                        + "  \"cargoList\": [],\n"
                        + "  \"truck\": null \n"
                        + "}")
                .when()
                .put("/delivery")
                .then()
                .body("fromLocation", equalTo("newString"))
                .body("id", notNullValue());
    }

    @Test
    public void testDeleteDilevery() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/delivery/" + delivery.getId())
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetAllCargo() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/cargo/allCargo").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size", is(1));
    }

    @Test
    public void testCreateCargo() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .body("{\n"
                        + "  \"name\": \"string\",\n"
                        + "  \"weight\": 0,\n"
                        + "  \"units\": 0,\n"
                        + "  \"dilevery\": null \n"
                        + "}")
                .when()
                .post("/cargo")
                .then()
                .statusCode(200);
    }

    @Test
    public void testEditCargo() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body("{\n"
                        + "  \"id\": " + cargo.getId() + ",\n"
                        + "  \"name\": \"newString\",\n"
                        + "  \"weight\": 0,\n"
                        + "  \"units\": 0,\n"
                        + "  \"dilevery\": null \n"
                        + "}")
                .when()
                .put("/cargo")
                .then()
                .body("name", equalTo("newString"))
                .body("id", notNullValue());
    }

    @Test
    public void testDeleteCargo() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/cargo/" + cargo.getId())
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetAllDrivers() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/driver/allDrivers").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size", is(1));
    }

    @Test
    public void testCreateDriver() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .body("{\n"
                        + "  \"name\": \"string\",\n"
                        + "  \"truck\": null \n"
                        + "}")
                .when()
                .post("/driver")
                .then()
                .statusCode(200);
    }

    @Test
    public void testEditDriver() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body("{\n"
                        + "  \"id\": " + driver.getId() + ",\n"
                        + "  \"name\": \"newString\",\n"
                        + "  \"truck\": null \n"
                        + "}")
                .when()
                .put("/driver")
                .then()
                .body("name", equalTo("newString"))
                .body("id", notNullValue());
    }

    @Test
    public void testDeleteDriver() {
        login("manager", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/driver/" + driver.getId())
                .then()
                .statusCode(200);
    }
    
//    @Test
//    public void testGetDriversByDate() {
//        given()
//                .contentType("application/json")
//                .accept(ContentType.JSON)
//                .when()
//                .body("{\n"
//                        + "  \"date\": \"2020-01-12\"\n"
//                        + "}")
//                .when()
//                .post("/driver/driversByDate")
//                .then()
//                .assertThat()
//                .statusCode(HttpStatus.OK_200.getStatusCode())
//                .body("size", is(1));
//    }
}
