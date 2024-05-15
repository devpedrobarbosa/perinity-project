package me.pedrobarbosa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.pedrobarbosa.dto.TaskDTO;
import me.pedrobarbosa.util.DateUtil;

import java.time.Duration;
import java.time.LocalDate;

/*
* Tarefa contendo ID, título, descrição, data de início e de término, duração, usuário alocado e se está terminada ou não
* */
@Entity
@Table(name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private Duration duration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean terminated = false;

    public TaskDTO toDTO() {
        Long userId = user == null ? null : user.getId();
        return new TaskDTO(id, title, description, DateUtil.writeDate(startDate), DateUtil.writeDate(endDate), duration.toHours(), department.getId(), userId, terminated);
    }
}