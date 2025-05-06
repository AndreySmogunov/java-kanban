package controllers;

import logic.TaskManager;
import models.Epic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/epics")
public class EpicController {

    private final TaskManager taskManager;

    @Autowired
    public EpicController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @GetMapping
    public ResponseEntity<List<Epic>> getAllEpics() {
        List<Epic> epics = taskManager.getAllEpics();
        return new ResponseEntity<>(epics, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Epic> getEpicById(@PathVariable int id) {
        Epic epic = taskManager.getEpicById(id);
        if (epic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(epic, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Epic> createEpic(@RequestBody Epic epic) {
        taskManager.createEpic(epic);
        return new ResponseEntity<>(epic, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Epic> updateEpic(@PathVariable int id, @RequestBody Epic epic) {
        epic.setId(id);
        taskManager.updateEpic(epic);
        return new ResponseEntity<>(epic, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEpic(@PathVariable int id) {
        taskManager.deleteEpicById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}