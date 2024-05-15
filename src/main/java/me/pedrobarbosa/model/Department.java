package me.pedrobarbosa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.pedrobarbosa.dto.DepartmentDTO;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

/*
* Departamento contendo ID, nome, lista de usuários e lista de tasks associados
* */
@Entity
@Table(name = "depts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department")
    @Fetch(FetchMode.JOIN)
    private List<User> users;

    @OneToMany(mappedBy = "department")
    @Fetch(FetchMode.JOIN)
    private List<Task> tasks;

    public DepartmentDTO toDTO() {
        int users = this.users == null ? 0 : this.users.size(), tasks = this.tasks == null ? 0 : this.tasks.size();
        return new DepartmentDTO(id, name, users, tasks);
    }
}