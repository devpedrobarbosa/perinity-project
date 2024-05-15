package me.pedrobarbosa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.pedrobarbosa.dto.AvgDTO;
import me.pedrobarbosa.dto.UserDTO;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/*
* Pessoa contendo ID, nome, departamento, e lista de tarefas associadas
* */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "user")
    @Fetch(value = FetchMode.JOIN)
    private List<Task> tasks;

    /*
    * Calcula a média de horas gastas por tarefa em um determinado período de tempo.
    * */
    public long calculateAverageHoursSpent(LocalDate startDate, LocalDate endDate) {
        long hoursSpent = 0;
        for(Task task : tasks) {
            LocalDate taskStart = task.getStartDate(), taskEnd = task.getEndDate();
            long taskDurationHours = task.getDuration().toHours();
            if((startDate.isBefore(taskStart) || startDate.isEqual(taskStart)) &&
                    (endDate.isAfter(taskEnd) || endDate.isEqual(taskEnd)))
                hoursSpent += taskDurationHours;
            else {
                LocalDate adjustedStart = taskStart.isBefore(startDate) ? startDate : taskStart,
                        adjustedEnd = taskEnd.isAfter(endDate) ? endDate : taskEnd;
                if(!adjustedStart.isAfter(adjustedEnd)) {
                    long adjustedDays = ChronoUnit.DAYS.between(adjustedStart, adjustedEnd) + 1,
                            adjustedHours = adjustedDays * 24;
                    hoursSpent += adjustedHours;
                }
            }
        }
        return hoursSpent / tasks.size();
    }

    public UserDTO toDTO() {
        long timeSpent = 0;
        for(Task task : tasks)
            timeSpent += task.getDuration().toHours();
        return new UserDTO(id, name, department.getId(), tasks.stream().map(Task::toDTO).toList(), timeSpent);
    }

    public AvgDTO toAvgDTO(AvgDTO avgDTO) {
        return new AvgDTO(id, name, avgDTO.startDate(), avgDTO.endDate(), tasks.size(), calculateAverageHoursSpent(avgDTO.getStartDate(), avgDTO.getEndDate()));
    }
}