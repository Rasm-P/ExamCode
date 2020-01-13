/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.CargoDTO;
import entities.Cargo;
import facades.CargoFacade;
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
@Path("cargo")
public class CargoResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final CargoFacade cargoFacade = CargoFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/allCargo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("manager")
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
    @RolesAllowed("manager")
    public CargoDTO createCargo(Cargo cargo) {
        Cargo newCargo = cargoFacade.createCargo(cargo);
        return new CargoDTO(newCargo);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("manager")
    public CargoDTO editCargo(Cargo cargo) {
        Cargo editCargo = cargoFacade.editCargo(cargo);
        return new CargoDTO(editCargo);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("manager")
    public CargoDTO deleteCargo(@PathParam("id") Long id) {
        Cargo deletedCargo = cargoFacade.removeCargo(id);
        return new CargoDTO(deletedCargo);
    }
}
