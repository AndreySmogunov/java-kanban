package controllers;

import logic.TaskManager;
import models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prioritized")
public class PrioritizedController {

    private final TaskManager taskManager;

    @Autowired
    public PrioritizedController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getPrioritizedTasks() {
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        return new ResponseEntity<>(prioritizedTasks, HttpStatus.OK);
    }
}