/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.DeliveryDTO;
import entities.Delivery;
import facades.DeliveryFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@OpenAPIDefinition(
        info = @Info(
                title = "ExamCode",
                version = "0.1",
                description = "Backend of the Sem3 Exam project"
        ),
        tags = {
            @Tag(name = "Delivery Resource", description = "Resource used for reading, adding, editing and deleting Delivery entities")
        },
        servers = {
            @Server(
                    description = "For Local host testing",
                    url = "http://localhost:8080/examCode"
            ),
            @Server(
                    description = "Server API",
                    url = "https://barfodpraetorius.dk/ExamCode"
            )

        }
)
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
    @Operation(summary = "Endpoint for admin roles to get a list of all Deliveries",
            tags = {"Delivery Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryDTO.class))),
                @ApiResponse(responseCode = "200", description = "A list of all deliveries is returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
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
    @Operation(summary = "Endpoint for admin roles to create a delivery",
            tags = {"Delivery Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryDTO.class))),
                @ApiResponse(responseCode = "200", description = "A delivery is created and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public DeliveryDTO createDelivery(Delivery delivery) {
        Delivery newDelivery = deliveryFacade.createDelivery(delivery);
        return new DeliveryDTO(newDelivery);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to edit a delivery",
            tags = {"Delivery Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryDTO.class))),
                @ApiResponse(responseCode = "200", description = "A delivery is edited and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public DeliveryDTO editDelivery(Delivery delivery) {
        Delivery editDelivery = deliveryFacade.editDelivery(delivery);
        return new DeliveryDTO(editDelivery);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to delete a delivery",
            tags = {"Delivery Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryDTO.class))),
                @ApiResponse(responseCode = "200", description = "A delivery is deleted and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public DeliveryDTO deleteDelivery(@PathParam("id") Long id) {
        Delivery deletedDelivery = deliveryFacade.removeDelivery(id);
        return new DeliveryDTO(deletedDelivery);
    }
}
