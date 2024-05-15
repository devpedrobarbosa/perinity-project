package me.pedrobarbosa.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import me.pedrobarbosa.dto.AvgDTO;
import me.pedrobarbosa.dto.UserDTO;
import me.pedrobarbosa.exception.*;
import me.pedrobarbosa.model.Department;
import me.pedrobarbosa.model.User;

import java.util.ArrayList;
import java.util.List;

/*
 * Serviço criado para lidar com as requisições do controlador de usuários
 * */
@ApplicationScoped
public class UserService implements Service<UserDTO> {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    public List<UserDTO> getAll() {
        return entityManager.createQuery("select u from User u", User.class).getResultList().stream().map(User::toDTO).toList();
    }

    @Override
    @Transactional
    public UserDTO find(Long id) {
        User user = entityManager.find(User.class, id);
        if(user == null) throw new UserNotFoundException();
        return user.toDTO();
    }

    /*
     * Busca um usuário através do nome e de um período espeficicado por startDate e endDate e retorna um objeto contendo
     * ID, nome, data de início e de término do período específico, total de tasks realizadas no período
     * e média de horas trabalhadas por task
     * */
    @Transactional
    public AvgDTO findUserAvg(AvgDTO avgDTO) {
        if(avgDTO.name() == null) throw new NullIDException();
        TypedQuery<User> query = entityManager.createQuery("select u from User u where name like ?1", User.class);
        query.setParameter(1, avgDTO.name());
        List<User> usersFound = query.getResultList();
        if(usersFound.isEmpty()) throw new UserNotFoundException();
        User user = usersFound.get(0);
        return user.toAvgDTO(avgDTO);
    }

    @Override
    @Transactional
    public UserDTO create(UserDTO userDTO) {
        if(userDTO.id() != null && entityManager.find(User.class, userDTO.id()) != null)
            throw new UserAlreadyExistsException();
        Department department = entityManager.find(Department.class, userDTO.departmentId());
        if(department == null)
            throw new DepartmentNotFoundException();
        User user = new User();
        user.setName(userDTO.name());
        user.setDepartment(department);
        if(userDTO.tasks() != null)
            user.setTasks(userDTO.getTasks(department));
        else user.setTasks(new ArrayList<>());
        entityManager.persist(user);
        return user.toDTO();
    }

    @Override
    @Transactional
    public UserDTO update(UserDTO userDTO) {
        if(userDTO.id() == null) throw new NullIDException();
        User user = entityManager.find(User.class, userDTO.id());
        if(user == null) throw new UserNotFoundException();
        if(userDTO.departmentId() != null) {
            Department department = entityManager.find(Department.class, userDTO.departmentId());
            if(department == null) throw new DepartmentAlreadyExistsException();
            user.setDepartment(department);
            user.getTasks().clear();
            user.setTasks(userDTO.getTasks(department));
        }
        if(userDTO.name() != null)
            user.setName(userDTO.name());
        return user.toDTO();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = entityManager.find(User.class, id);
        if(user == null) throw new UserNotFoundException();
        entityManager.remove(user);
    }
}