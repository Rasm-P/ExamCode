/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.TruckDTO;
import entities.Truck;
import facades.TruckFacade;
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
            @Tag(name = "Truck Resource", description = "Resource used for reading, adding, editing and deleting Truck entities")
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
@Path("truck")
public class TruckResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final TruckFacade truckFacade = TruckFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/allTrucks")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Endpoint that returns a list of all trucks",
            tags = {"Truck Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = TruckDTO.class))),
                @ApiResponse(responseCode = "200", description = "A list of all trucks is returned")})
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
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to create a truck",
            tags = {"Truck Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = TruckDTO.class))),
                @ApiResponse(responseCode = "200", description = "A truck is created and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public TruckDTO createTruck(Truck truck) {
        Truck newTruck = truckFacade.createTruck(truck);
        return new TruckDTO(newTruck);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to edit a truck",
            tags = {"Truck Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = TruckDTO.class))),
                @ApiResponse(responseCode = "200", description = "A truck is edited and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public TruckDTO editTruck(Truck truck) {
        Truck editTruck = truckFacade.editTruck(truck);
        return new TruckDTO(editTruck);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to delete a truck",
            tags = {"Truck Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = TruckDTO.class))),
                @ApiResponse(responseCode = "200", description = "A truck is deleted and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public TruckDTO deleteTruck(@PathParam("id") Long id) {
        Truck deletedTruck = truckFacade.removeTruck(id);
        return new TruckDTO(deletedTruck);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/trucksByDate/{date}")
    @Operation(summary = "Endpoint that returns a list of all Trucks that are booked on a specific date",
            tags = {"Truck Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = TruckDTO.class))),
                @ApiResponse(responseCode = "200", description = "A list of all trucks that are booked is returned")})
    public List<TruckDTO> getBookedTrucksByDate(@PathParam("date") String date) {
        List<Truck> truck = truckFacade.getBookedTrucksByDate(date);
        List<TruckDTO> dto = new ArrayList<>();
        for (Truck t : truck) {
            dto.add(new TruckDTO(t));
        }
        return dto;
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/trucksNonBookedByDate/{date}")
//    public List<TruckDTO> getNonBookedTrucksByDate(@PathParam("date") String date) {
//        List<Truck> truck = truckFacade.getNonBookedTrucksByDate(date);
//        List<TruckDTO> dto = new ArrayList<>();
//        for (Truck t : truck) {
//            dto.add(new TruckDTO(t));
//        }
//        return dto;
//    }
}
