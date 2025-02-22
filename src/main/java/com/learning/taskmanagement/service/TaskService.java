package com.learning.taskmanagement.service;

import com.learning.taskmanagement.dto.TaskDTO;
import com.learning.taskmanagement.domain.TaskStatus;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

/**
 * Service interface for Task operations.
 * 
 * Learning Points:
 * 1. Interface segregation principle - define cohesive interfaces
 * 2. Using Optional for nullable returns
 * 3. Working with DTOs instead of entities at service level
 */
public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO);
    Optional<TaskDTO> getTask(UUID id);
    List<TaskDTO> getAllTasks();
    List<TaskDTO> getTasksByStatus(TaskStatus status);
    TaskDTO updateTask(UUID id, TaskDTO taskDTO);
    void deleteTask(UUID id);
}
