package controllers;

import logic.TaskManager;
import models.Subtask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subtasks")
public class SubtaskController {

    private final TaskManager taskManager;

    @Autowired
    public SubtaskController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @GetMapping
    public ResponseEntity<List<Subtask>> getAllSubtasks() {
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        return new ResponseEntity<>(subtasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subtask> getSubtaskById(@PathVariable int id) {
        Subtask subtask = taskManager.getSubtaskById(id);
        if (subtask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subtask, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Subtask> createSubtask(@RequestBody Subtask subtask) {
        try {
            taskManager.createSubtask(subtask);
            return new ResponseEntity<>(subtask, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subtask> updateSubtask(@PathVariable int id, @RequestBody Subtask subtask) {
        subtask.setId(id);
        try {
            taskManager.updateSubtask(subtask);
            return new ResponseEntity<>(subtask, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable int id) {
        taskManager.deleteSubtaskById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}