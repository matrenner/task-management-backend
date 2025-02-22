package com.learning.taskmanagement.repository;

import com.learning.taskmanagement.domain.Task;
import com.learning.taskmanagement.domain.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for TaskRepository.
 * 
 * Learning Points:
 * 1. Using TestContainers for database testing
 * 2. @DataJpaTest for repository layer testing
 * 3. Testing database operations
 * 4. Dynamic test properties configuration
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.2");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void findByStatus_ShouldReturnMatchingTasks() {
        // Arrange
        Task task = new Task("Test Task");
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskRepository.save(task);

        // Act
        List<Task> found = taskRepository.findByStatus(TaskStatus.IN_PROGRESS);

        // Assert
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void findByDueDateBefore_ShouldReturnOverdueTasks() {
        // Arrange
        Task task = new Task("Overdue Task");
        task.setDueDate(LocalDateTime.now().minusDays(1));
        taskRepository.save(task);

        // Act
        List<Task> found = taskRepository.findByDueDateBefore(LocalDateTime.now());

        // Assert
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo("Overdue Task");
    }

    @Test
    void findByStatus_WhenNoMatchingTasks_ShouldReturnEmptyList() {
        // Arrange
        Task task = new Task("Test Task");
        task.setStatus(TaskStatus.TODO);
        taskRepository.save(task);

        // Act
        List<Task> found = taskRepository.findByStatus(TaskStatus.COMPLETED);

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void findByDueDateBefore_WithFutureDate_ShouldNotReturnFutureTasks() {
        // Arrange
        Task futureTask = new Task("Future Task");
        futureTask.setDueDate(LocalDateTime.now().plusDays(1));
        taskRepository.save(futureTask);

        // Act
        List<Task> found = taskRepository.findByDueDateBefore(LocalDateTime.now());

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void savingTask_ShouldSetCreatedAndUpdatedDates() {
        // Arrange
        Task task = new Task("Test Task");
        LocalDateTime beforeSave = LocalDateTime.now();

        // Act
        Task savedTask = taskRepository.save(task);
        LocalDateTime afterSave = LocalDateTime.now();

        // Assert
        assertThat(savedTask.getCreatedAt()).isNotNull()
                .isAfterOrEqualTo(beforeSave)
                .isBeforeOrEqualTo(afterSave);
        assertThat(savedTask.getUpdatedAt()).isNotNull()
                .isAfterOrEqualTo(beforeSave)
                .isBeforeOrEqualTo(afterSave);
    }
}
