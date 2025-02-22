package com.learning.taskmanagement.repository;

import com.learning.taskmanagement.domain.Task;
import com.learning.taskmanagement.domain.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Task entities.
 * 
 * Learning Points:
 * 1. Spring Data JPA provides basic CRUD operations out of the box
 * 2. Custom queries can be defined using method names
 * 3. @Repository marks this as a Spring component for persistence operations
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    // Spring Data JPA will implement these methods automatically
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByDueDateBefore(LocalDateTime date);
}
