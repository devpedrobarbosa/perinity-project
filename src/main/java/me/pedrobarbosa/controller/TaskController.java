package me.pedrobarbosa.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import me.pedrobarbosa.dto.TaskDTO;
import me.pedrobarbosa.service.TaskService;

import java.util.List;

@Path("/tarefas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskController implements Controller<TaskDTO> {

    @Inject
    TaskService taskService;

    @Override
    @GET
    public List<TaskDTO> getAll() {
        return taskService.getAll();
    }

    /*
     * Realiza a requisição da lista das 3 tarefas pendentes sem usuário
     * com os prazos mais antigos em ordem decrescente
     * */
    @GET
    @Path("/pendentes")
    public List<TaskDTO> getPendingTasks() {
        return taskService.getPendingTasks();
    }

    @Override
    @GET
    @Path("/{id}")
    public TaskDTO find(@PathParam("id") Long id) {
        return taskService.find(id);
    }

    @Override
    @POST
    public TaskDTO create(TaskDTO taskDTO) {
        return taskService.create(taskDTO);
    }

    @Override
    @PUT
    public TaskDTO update(TaskDTO taskDTO) {
        return taskService.update(taskDTO);
    }

    /*
     * Realiza a requisição da alocação de um usuário em uma task de seu departamento
     * */
    @PUT
    @Path("/alocar/{id}")
    public TaskDTO allocateUser(@PathParam("id") Long id) {
        return taskService.allocateUser(id);
    }

    /*
     * Realiza a requisição da finalização uma task existente
     * */
    @PUT
    @Path("/finalizar/{id}")
    public TaskDTO terminateTask(@PathParam("id") Long id) {
        return taskService.terminateTask(id);
    }

    @Override
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        taskService.delete(id);
    }
}