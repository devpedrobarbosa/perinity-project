package me.pedrobarbosa.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import me.pedrobarbosa.dto.DepartmentDTO;
import me.pedrobarbosa.exception.DepartmentAlreadyExistsException;
import me.pedrobarbosa.exception.DepartmentNotFoundException;
import me.pedrobarbosa.exception.NullIDException;
import me.pedrobarbosa.model.Department;

import java.util.List;

/*
 * Serviço criado para lidar com as requisições do controlador de departamentos
 * */
@ApplicationScoped
public class DepartmentService implements Service<DepartmentDTO> {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    public List<DepartmentDTO> getAll() {
        return entityManager.createQuery("select d from Department d", Department.class).getResultList().stream().map(Department::toDTO).toList();
    }

    @Override
    @Transactional
    public DepartmentDTO find(Long id) {
        Department department = entityManager.find(Department.class, id);
        if(department == null) throw new DepartmentNotFoundException();
        return department.toDTO();
    }

    @Override
    @Transactional
    public DepartmentDTO create(DepartmentDTO departmentDTO) {
        if(departmentDTO.name() == null) throw new IllegalArgumentException("O nome do departamento não pode ser nulo");
        TypedQuery<Department> query = entityManager.createQuery("select d from Department d where name like ?1", Department.class);
        query.setParameter(1, departmentDTO.name());
        if(!query.getResultList().isEmpty()) throw new DepartmentAlreadyExistsException();
        Department department = new Department();
        department.setName(departmentDTO.name());
        entityManager.persist(department);
        return department.toDTO();
    }

    @Override
    @Transactional
    public DepartmentDTO update(DepartmentDTO departmentDTO) {
        if(departmentDTO.id() == null) throw new NullIDException();
        Department department = entityManager.find(Department.class, departmentDTO.id());
        if(department == null) throw new DepartmentNotFoundException();
        department.setName(departmentDTO.name());
        return department.toDTO();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Department department = entityManager.find(Department.class, id);
        if(department == null) throw new DepartmentNotFoundException();
        entityManager.remove(department);
    }
}