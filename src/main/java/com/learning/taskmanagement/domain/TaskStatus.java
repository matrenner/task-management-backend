package com.learning.taskmanagement.domain;

/**
 * Enum representing the possible states of a Task.
 * 
 * Learning Points:
 * 1. Using enums provides type safety and prevents invalid status values
 * 2. Enums are perfect for representing a fixed set of values
 */
public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
