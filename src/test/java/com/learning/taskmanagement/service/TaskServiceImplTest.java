package com.learning.taskmanagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.learning.taskmanagement.domain.Task;
import com.learning.taskmanagement.domain.TaskStatus;
import com.learning.taskmanagement.dto.TaskDTO;
import com.learning.taskmanagement.exception.TaskNotFoundException;
import com.learning.taskmanagement.repository.TaskRepository;

/**
 * Unit tests for TaskServiceImpl.
 * 
 * Learning Points:
 * 1. Using @ExtendWith for Mockito integration
 * 2. Proper test isolation with @BeforeEach
 * 3. Testing both success and failure scenarios
 * 4. Using AssertJ for fluent assertions
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    void createTask_ShouldReturnNewTask() {
        // Arrange
        TaskDTO inputDto = new TaskDTO(null, "Test Task", "Description", null, null, null, null);
        Task savedTask = new Task("Test Task");
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        TaskDTO result = taskService.createTask(inputDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Task");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.TODO);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void getTask_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        UUID id = UUID.randomUUID();
        Task task = new Task("Test Task");
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        // Act
        Optional<TaskDTO> result = taskService.getTask(id);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Task");
        verify(taskRepository).findById(id);
    }

    @Test
    void updateTask_WhenTaskDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        TaskDTO updateDto = new TaskDTO(id, "Updated Task", "Description", TaskStatus.IN_PROGRESS, null, null, null);
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> taskService.updateTask(id, updateDto))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void getTasksByStatus_ShouldReturnFilteredTasks() {
        // Arrange
        Task task = new Task("Test Task");
        task.setStatus(TaskStatus.IN_PROGRESS);
        when(taskRepository.findByStatus(TaskStatus.IN_PROGRESS))
                .thenReturn(List.of(task));

        // Act
        List<TaskDTO> results = taskService.getTasksByStatus(TaskStatus.IN_PROGRESS);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository).findByStatus(TaskStatus.IN_PROGRESS);
    }

    @Test
    void createTask_WithNullTitle_ShouldThrowException() {
        // Arrange
        TaskDTO inputDto = new TaskDTO(null, null, "Description", null, null, null, null);

        // Act & Assert
        assertThatThrownBy(() -> taskService.createTask(inputDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Title");
    }

    @Test
    void updateTask_WhenTaskExists_ShouldUpdateAllFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        Task existingTask = new Task("Original Title");
        TaskDTO updateDto = new TaskDTO(id, "Updated Title", "New Description", 
                TaskStatus.IN_PROGRESS, LocalDateTime.now().plusDays(1), null, null);
        
        when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // Act
        TaskDTO result = taskService.updateTask(id, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDelete() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(taskRepository.existsById(id)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(id);

        // Act & Assert
        assertThatCode(() -> taskService.deleteTask(id))
                .doesNotThrowAnyException();
        verify(taskRepository).deleteById(id);
    }

    @Test
    void getAllTasks_WhenNoTasks_ShouldReturnEmptyList() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(List.of());

        // Act
        List<TaskDTO> result = taskService.getAllTasks();

        // Assert
        assertThat(result).isEmpty();
    }
}
