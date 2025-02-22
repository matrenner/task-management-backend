package com.learning.taskmanagement.exception;

import java.util.UUID;

/**
 * Custom exception for task not found cases.
 * 
 * Learning Points:
 * 1. Custom exceptions help create meaningful error messages
 * 2. Extending RuntimeException for unchecked exceptions
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID id) {
        super("Task not found with id: " + id);
    }
}
