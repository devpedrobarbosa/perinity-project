package me.pedrobarbosa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.pedrobarbosa.model.Department;
import me.pedrobarbosa.model.Task;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id;
    private String title;
    private String desc;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String startDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String endDate;
    private Long duration;
    private Long departmentId;
    private Long userId;
    private Boolean terminated;

    public LocalDate getStartDate() {
        return startDate == null ? null : LocalDate.parse(startDate, DateTimeFormatter.ofPattern("d/MM/yyyy"));
    }

    public LocalDate getEndDate() {
        return endDate == null ? null : LocalDate.parse(endDate, DateTimeFormatter.ofPattern("d/MM/yyyy"));
    }

    public Task toTask(Department department) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(desc);
        task.setStartDate(getStartDate());
        task.setEndDate(getEndDate());
        task.setDepartment(department);
        task.setDuration(Duration.between(task.getStartDate().atStartOfDay(), task.getEndDate().atStartOfDay()));
        return task;
    }
}