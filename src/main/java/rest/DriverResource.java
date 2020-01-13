/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.DriverDTO;
import entities.Driver;
import facades.DriverFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

/**
 * REST Web Service
 *
 * @author rasmu
 */
@Path("driver")
public class DriverResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final DriverFacade driverFacade = DriverFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/allDrivers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DriverDTO> getAllDrivers() {
        List<Driver> driver = driverFacade.getAllDrivers();
        List<DriverDTO> dto = new ArrayList<>();
        for (Driver d : driver) {
            dto.add(new DriverDTO(d));
        }
        return dto;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("manager")
    public DriverDTO createDriver(Driver driver) {
        Driver newDriver = driverFacade.createDriver(driver);
        return new DriverDTO(newDriver);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("manager")
    public DriverDTO editDriver(Driver driver) {
        Driver editDriver = driverFacade.editDriver(driver);
        return new DriverDTO(editDriver);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("manager")
    public DriverDTO deleteDriver(@PathParam("id") Long id) {
        Driver deletedDriver = driverFacade.removeDriver(id);
        return new DriverDTO(deletedDriver);
    }
}
