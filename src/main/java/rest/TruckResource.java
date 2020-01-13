/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.TruckDTO;
import entities.Truck;
import facades.TruckFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
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
@Path("truck")
public class TruckResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final TruckFacade truckFacade = TruckFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;
    
    @GET
    @Path("/allTrucks")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TruckDTO> getAllTrucks() {
        List<Truck> truck = truckFacade.getAllTrucks();
        List<TruckDTO> dto = new ArrayList<>();
        for (Truck t : truck) {
            dto.add(new TruckDTO(t));
        }
        return dto;
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("manager")
    public TruckDTO createTruck(Truck truck) {
        Truck newTruck = truckFacade.createTruck(truck);
        return new TruckDTO(newTruck);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("manager")
    public TruckDTO editHobby(Truck truck) {
        Truck editTruck = truckFacade.editTruck(truck);
        return new TruckDTO(editTruck);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin")
    public TruckDTO deleteHobby(@PathParam("id") Long id) {
        Truck deletedTruck = truckFacade.removeTruck(id);
        return new TruckDTO(deletedTruck);
    }
}
