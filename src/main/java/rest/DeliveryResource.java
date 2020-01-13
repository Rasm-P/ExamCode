/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.DeliveryDTO;
import entities.Delivery;
import facades.DeliveryFacade;
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
@Path("delivery")
public class DeliveryResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final DeliveryFacade deliveryFacade = DeliveryFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/allDeliveries")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<DeliveryDTO> getAllDeliveries() {
        List<Delivery> delivery = deliveryFacade.getAllDeliveries();
        List<DeliveryDTO> dto = new ArrayList<>();
        for (Delivery d : delivery) {
            dto.add(new DeliveryDTO(d));
        }
        return dto;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public DeliveryDTO createDelivery(Delivery delivery) {
        Delivery newDelivery = deliveryFacade.createDelivery(delivery);
        return new DeliveryDTO(newDelivery);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public DeliveryDTO editDelivery(Delivery delivery) {
        Delivery editDelivery = deliveryFacade.editDelivery(delivery);
        return new DeliveryDTO(editDelivery);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin")
    public DeliveryDTO deleteDelivery(@PathParam("id") Long id) {
        Delivery deletedDelivery = deliveryFacade.removeDelivery(id);
        return new DeliveryDTO(deletedDelivery);
    }
}
