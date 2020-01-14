/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.CargoDTO;
import entities.Cargo;
import facades.CargoFacade;
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
            @Tag(name = "Cargo Resource", description = "Resource used for reading, adding, editing and deleting Cargo entities")
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
@Path("cargo")
public class CargoResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final CargoFacade cargoFacade = CargoFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/allCargo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to get a list of all Cargo",
            tags = {"Cargo Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CargoDTO.class))),
                @ApiResponse(responseCode = "200", description = "A list of all cargo is returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public List<CargoDTO> getAllCargo() {
        List<Cargo> cargo = cargoFacade.getAllCargo();
        List<CargoDTO> dto = new ArrayList<>();
        for (Cargo c : cargo) {
            dto.add(new CargoDTO(c));
        }
        return dto;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to create cargo",
            tags = {"Cargo Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CargoDTO.class))),
                @ApiResponse(responseCode = "200", description = "Cargo object was created and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public CargoDTO createCargo(Cargo cargo) {
        Cargo newCargo = cargoFacade.createCargo(cargo);
        return new CargoDTO(newCargo);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to edit cargo",
            tags = {"Cargo Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CargoDTO.class))),
                @ApiResponse(responseCode = "200", description = "Cargo object was edited and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public CargoDTO editCargo(Cargo cargo) {
        Cargo editCargo = cargoFacade.editCargo(cargo);
        return new CargoDTO(editCargo);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to delete cargo",
            tags = {"Cargo Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CargoDTO.class))),
                @ApiResponse(responseCode = "200", description = "Cargo object was deleted and returned"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public CargoDTO deleteCargo(@PathParam("id") Long id) {
        Cargo deletedCargo = cargoFacade.removeCargo(id);
        return new CargoDTO(deletedCargo);
    }
}
