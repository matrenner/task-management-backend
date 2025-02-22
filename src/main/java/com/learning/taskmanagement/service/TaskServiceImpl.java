package com.learning.taskmanagement.service;

import com.learning.taskmanagement.domain.Task;
import com.learning.taskmanagement.domain.TaskStatus;
import com.learning.taskmanagement.dto.TaskDTO;
import com.learning.taskmanagement.repository.TaskRepository;
import com.learning.taskmanagement.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of TaskService.
 * 
 * Learning Points:
 * 1. @Transactional for data consistency
 * 2. Mapping between DTOs and entities
 * 3. Exception handling for business logic
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        return mapToDTO(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskDTO> getTask(UUID id) {
        return taskRepository.findById(id).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(UUID id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setDueDate(taskDTO.getDueDate());
        
        return mapToDTO(taskRepository.save(task));
    }

    @Override
    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
    }

    private TaskDTO mapToDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getDueDate(),
            task.getCreatedAt(),
            task.getUpdatedAt()
        );
    }
}
