package me.pedrobarbosa.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import me.pedrobarbosa.dto.TaskDTO;
import me.pedrobarbosa.exception.*;
import me.pedrobarbosa.model.Department;
import me.pedrobarbosa.model.Task;
import me.pedrobarbosa.model.User;

import java.time.Duration;
import java.util.List;

/*
 * Serviço criado para lidar com as requisições do controlador de tarefas
 * */
@ApplicationScoped
public class TaskService implements Service<TaskDTO> {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    public List<TaskDTO> getAll() {
        return entityManager.createQuery("select t from Task t", Task.class).getResultList().stream().map(Task::toDTO).toList();
    }

    /*
     * Retorna as 3 tarefas sem pessoas alocadas com os prazos de vencimento mais antigos em ordem decrescente
     * */
    @Transactional
    public List<TaskDTO> getPendingTasks() {
        TypedQuery<Task> query = entityManager.createQuery("select t from Task t where t.terminated is false and t.user is null order by endDate desc limit 3", Task.class);
        return query.getResultList().stream().map(Task::toDTO).toList();
    }

    @Override
    @Transactional
    public TaskDTO find(Long id) {
        Task task = entityManager.find(Task.class, id);
        if(task == null) throw new TaskNotFoundException();
        return task.toDTO();
    }

    @Override
    @Transactional
    public TaskDTO create(TaskDTO taskDTO) {
        if(taskDTO.getId() != null && entityManager.find(Task.class, taskDTO.getId()) != null)
            throw new TaskAlreadyExistsException();
        Department department = entityManager.find(Department.class, taskDTO.getDepartmentId());
        if(department == null) throw new DepartmentNotFoundException();
        Task task = taskDTO.toTask(department);
        if(taskDTO.getUserId() != null) {
            User user = entityManager.find(User.class, taskDTO.getUserId());
            if(user == null) throw new UserNotFoundException();
            task.setUser(user);
        }
        entityManager.persist(task);
        return task.toDTO();
    }

    @Override
    @Transactional
    public TaskDTO update(TaskDTO taskDTO) {
        if(taskDTO.getId() == null) throw new NullIDException();
        Task task = entityManager.find(Task.class, taskDTO.getId());
        if(task == null) throw new TaskNotFoundException();
        if(taskDTO.getTitle() != null)
            task.setTitle(taskDTO.getTitle());
        if(taskDTO.getDesc() != null)
            task.setDescription(taskDTO.getDesc());
        if(taskDTO.getStartDate() != null)
            task.setStartDate(taskDTO.getStartDate());
        if(taskDTO.getEndDate() != null)
            task.setEndDate(taskDTO.getEndDate());
        if(taskDTO.getStartDate() != null || taskDTO.getEndDate() != null)
            task.setDuration(Duration.between(task.getStartDate(), task.getEndDate()));
        if(taskDTO.getDepartmentId() != null) {
            Department department = entityManager.find(Department.class, taskDTO.getDepartmentId());
            if(department == null) throw new DepartmentNotFoundException();
            task.setDepartment(department);
        }
        if(taskDTO.getUserId() != null) {
            User user = entityManager.find(User.class, taskDTO.getUserId());
            if(user == null) throw new UserNotFoundException();
            task.setUser(user);
        }
        if(taskDTO.getTerminated() != null)
            task.setTerminated(taskDTO.getTerminated());
        return task.toDTO();
    }

    /*
     * Aloca o usuário em alguma terefa do seu respectivo departamento que esteja aberta
     * Se houver uma terafa sem usuário, será priorizada
     * Caso contrário, o usuário alocado da primeira tarefa será substituído
     * Caso não haja nenhuma tarefa aberta no departamento, retorna exceção
     * */
    @Transactional
    public TaskDTO allocateUser(Long id) {
        User user = entityManager.find(User.class, id);
        if(user == null) throw new UserNotFoundException();
        Department department = user.getDepartment();
        TypedQuery<Task> query = entityManager.createQuery("select t from Task t where department = ?1", Task.class);
        query.setParameter(1, department);
        List<Task> tasksFound = query.getResultList();
        if(tasksFound.isEmpty())
            throw new TaskNotFoundException();
        Task task = tasksFound.stream().filter(t -> !t.isTerminated() && t.getUser() == null).findAny().orElse(tasksFound.get(0));
        task.setUser(user);
        return task.toDTO();
    }

    @Transactional
    public TaskDTO terminateTask(Long id) {
        Task task = entityManager.find(Task.class, id);
        if(task == null) throw new TaskNotFoundException();
        if(task.isTerminated()) throw new TaskTerminatedException();
        task.setTerminated(true);
        return task.toDTO();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Task task = entityManager.find(Task.class, id);
        if(task == null) throw new TaskNotFoundException();
        entityManager.remove(task);
    }
}