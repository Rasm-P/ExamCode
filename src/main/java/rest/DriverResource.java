/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.DriverDTO;
import entities.Driver;
import facades.DriverFacade;
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
            @Tag(name = "Driver Resource", description = "Resource used for reading, adding, editing and deleting Driver entities")
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
@Path("driver")
public class DriverResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final DriverFacade driverFacade = DriverFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/allDrivers")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Endpoint that returns a list of all Drivers",
            tags = {"Driver Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverDTO.class))),
                @ApiResponse(responseCode = "200", description = "A list of all drivers is returned")})
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
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to create a driver",
            tags = {"Driver Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverDTO.class))),
                @ApiResponse(responseCode = "200", description = "A driver is created and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public DriverDTO createDriver(Driver driver) {
        Driver newDriver = driverFacade.createDriver(driver);
        return new DriverDTO(newDriver);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to edit a driver",
            tags = {"Driver Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverDTO.class))),
                @ApiResponse(responseCode = "200", description = "A driver is edited and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public DriverDTO editDriver(Driver driver) {
        Driver editDriver = driverFacade.editDriver(driver);
        return new DriverDTO(editDriver);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to delete a driver",
            tags = {"Driver Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverDTO.class))),
                @ApiResponse(responseCode = "200", description = "A driver is deleted and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public DriverDTO deleteDriver(@PathParam("id") Long id) {
        Driver deletedDriver = driverFacade.removeDriver(id);
        return new DriverDTO(deletedDriver);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/driversByDate/{date}")
    @Operation(summary = "Endpoint that returns a list of all Drivers that are booked on a specific date",
            tags = {"Driver Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverDTO.class))),
                @ApiResponse(responseCode = "200", description = "A list of all drivers that are booked is returned")})
    public List<DriverDTO> getBookedDriversByDate(@PathParam("date") String date) {
        List<Driver> driver = driverFacade.getBookedDriversByDate(date);
        List<DriverDTO> dto = new ArrayList<>();
        for (Driver d : driver) {
            dto.add(new DriverDTO(d));
        }
        return dto;
    }
}
