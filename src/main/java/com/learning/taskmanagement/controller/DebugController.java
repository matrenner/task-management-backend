package com.learning.taskmanagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {
    private static final Logger logger = LoggerFactory.getLogger(DebugController.class);

    @GetMapping("/debug/routing")
    public String testRouting() {
        logger.info("Debug routing endpoint hit");
        return "Routing is working";
    }
}
