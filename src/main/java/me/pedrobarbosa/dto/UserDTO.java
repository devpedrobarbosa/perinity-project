package me.pedrobarbosa.dto;

import me.pedrobarbosa.model.Department;
import me.pedrobarbosa.model.Task;

import java.util.List;

public record UserDTO(Long id, String name, Long departmentId, List<TaskDTO> tasks, Long timeSpent) {

    public List<Task> getTasks(Department department) {
        return tasks.stream().map(taskDTO -> taskDTO.toTask(department)).toList();
    }
}