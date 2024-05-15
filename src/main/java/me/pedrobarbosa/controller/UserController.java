package me.pedrobarbosa.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import me.pedrobarbosa.dto.AvgDTO;
import me.pedrobarbosa.dto.UserDTO;
import me.pedrobarbosa.service.UserService;

import java.util.List;

@Path("/pessoas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController implements Controller<UserDTO> {

    @Inject
    UserService userService;

    @Override
    @GET
    public List<UserDTO> getAll() {
        return userService.getAll();
    }

    @Override
    @GET
    @Path("/{id}")
    public UserDTO find(@PathParam("id") Long id) {
        return userService.find(id);
    }

    /*
     * Realiza a requisição da média de horas gastas por tarefa de um usuário em um período de tempo
     * */
    @GET
    @Path("/gastos")
    public AvgDTO findUserAvg(AvgDTO avgDTO) {
        return userService.findUserAvg(avgDTO);
    }

    @Override
    @POST
    public UserDTO create(UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @Override
    @PUT
    public UserDTO update(UserDTO userDTO) {
        return userService.update(userDTO);
    }

    @Override
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        userService.delete(id);
    }
}