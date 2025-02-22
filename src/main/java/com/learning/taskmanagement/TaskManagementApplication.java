package com.learning.taskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main entry point for the Task Management application.
 * 
 * Learning Points:
 * 1. @SpringBootApplication is a convenience annotation that combines:
 *    - @Configuration: Tags the class as a source of bean definitions
 *    - @EnableAutoConfiguration: Tells Spring Boot to auto-configure the application
 *    - @ComponentScan: Tells Spring to look for components in the current package
 * 
 * 2. This class follows the Single Responsibility Principle by only being responsible
 *    for bootstrapping the application.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.learning.taskmanagement",
    "com.learning.taskmanagement.config"
})
public class TaskManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApplication.class, args);
    }
}
