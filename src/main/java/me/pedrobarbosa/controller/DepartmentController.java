package me.pedrobarbosa.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import me.pedrobarbosa.dto.DepartmentDTO;
import me.pedrobarbosa.service.DepartmentService;

import java.util.List;

@Path("/departamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentController implements Controller<DepartmentDTO> {

    @Inject
    DepartmentService departmentService;

    @Override
    @GET
    public List<DepartmentDTO> getAll() {
        return departmentService.getAll();
    }

    @Override
    @GET
    @Path("/{id}")
    public DepartmentDTO find(@PathParam("id") Long id) {
        return departmentService.find(id);
    }

    @Override
    @POST
    public DepartmentDTO create(DepartmentDTO departmentDTO) {
        return departmentService.create(departmentDTO);
    }

    @Override
    @PUT
    public DepartmentDTO update(DepartmentDTO departmentDTO) {
        return departmentService.update(departmentDTO);
    }

    @Override
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        departmentService.delete(id);
    }
}
