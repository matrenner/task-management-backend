package com.learning.taskmanagement.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.taskmanagement.domain.TaskStatus;
import com.learning.taskmanagement.dto.TaskDTO;
import com.learning.taskmanagement.exception.TaskNotFoundException;
import com.learning.taskmanagement.service.TaskService;

/**
 * Unit tests for TaskController.
 * 
 * Learning Points:
 * 1. Using @WebMvcTest for testing MVC controllers
 * 2. MockMvc for simulating HTTP requests
 * 3. Testing REST endpoints
 * 4. Verifying JSON responses
 */
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        // Arrange
        TaskDTO inputDto = new TaskDTO(null, "Test Task", "Description", null, null, null, null);
        TaskDTO savedDto = new TaskDTO(UUID.randomUUID(), "Test Task", "Description", 
                TaskStatus.TODO, null, null, null);
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(savedDto);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void createTask_WithInvalidData_ShouldReturn400() throws Exception {
        // Arrange
        TaskDTO inputDto = new TaskDTO(null, "", "Description", null, null, null, null);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTask_WhenTaskExists_ShouldReturnTask() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        TaskDTO taskDTO = new TaskDTO(id, "Test Task", "Description", 
                TaskStatus.TODO, null, null, null);
        when(taskService.getTask(id)).thenReturn(Optional.of(taskDTO));

        // Act & Assert
        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void getTask_WhenTaskDoesNotExist_ShouldReturn404() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        when(taskService.getTask(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks() throws Exception {
        // Arrange
        TaskDTO task = new TaskDTO(UUID.randomUUID(), "Test Task", "Description", 
                TaskStatus.TODO, null, null, null);
        when(taskService.getAllTasks()).thenReturn(List.of(task));

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].status").value("TODO"));
    }

    @Test
    void updateTask_WhenTaskNotFound_ShouldReturn404() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        TaskDTO updateDto = new TaskDTO(id, "Updated Task", "Description", 
                TaskStatus.IN_PROGRESS, null, null, null);
        when(taskService.updateTask(eq(id), any(TaskDTO.class)))
                .thenThrow(new TaskNotFoundException(id));

        // Act & Assert
        mockMvc.perform(put("/api/tasks/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTasksByStatus_ShouldReturnFilteredTasks() throws Exception {
        // Arrange
        TaskDTO task = new TaskDTO(UUID.randomUUID(), "Test Task", "Description", 
                TaskStatus.IN_PROGRESS, null, null, null);
        when(taskService.getTasksByStatus(TaskStatus.IN_PROGRESS))
                .thenReturn(List.of(task));

        // Act & Assert
        mockMvc.perform(get("/api/tasks")
                .param("status", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void deleteTask_WhenSuccessful_ShouldReturn204() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(taskService).deleteTask(id);

        // Act & Assert
        mockMvc.perform(delete("/api/tasks/" + id))
                .andExpect(status().isNoContent());
    }
}
