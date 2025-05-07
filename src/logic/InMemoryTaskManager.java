package logic;

import exceptions.NotFoundException;
import models.Epic;
import models.PrioritizedTask;
import models.Subtask;
import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private HistoryManager historyManager;
    private TreeSet<PrioritizedTask> prioritizedTasks = new TreeSet<>();

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public InMemoryTaskManager() {

    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Task not found");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public void createTask(Task task) {
        if (isTaskOverlapping(task)) {
            throw new IllegalArgumentException("Task overlaps with existing tasks");
        }
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(new PrioritizedTask(task));
        }
    }

    @Override
    public void updateTask(Task task) {
        if (isTaskOverlapping(task)) {
            throw new IllegalArgumentException("Task overlaps with existing tasks");
        }
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(new PrioritizedTask(task));
        }
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null && task.getStartTime() != null) {
            prioritizedTasks.remove(new PrioritizedTask(task));
        }
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            throw new NotFoundException("Subtask not found");
        }
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (isTaskOverlapping(subtask)) {
            throw new IllegalArgumentException("Subtask overlaps with existing tasks");
        }
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
        }
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(new PrioritizedTask(subtask));
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (isTaskOverlapping(subtask)) {
            throw new IllegalArgumentException("Subtask overlaps with existing tasks");
        }
        subtasks.put(subtask.getId(), subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(new PrioritizedTask(subtask));
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
            }
            if (subtask.getStartTime() != null) {
                prioritizedTasks.remove(new PrioritizedTask(subtask));
            }
        }
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException("Epic not found");
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            epic.getSubtaskIds().forEach(subtaskId -> {
                Subtask subtask = subtasks.remove(subtaskId);
                if (subtask != null && subtask.getStartTime() != null) {
                    prioritizedTasks.remove(new PrioritizedTask(subtask));
                }
            });
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        return epics.get(epicId).getSubtaskIds().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream()
                .map(PrioritizedTask::getTask)
                .collect(Collectors.toList());
    }

    private boolean isTaskOverlapping(Task task) {
        return Stream.concat(tasks.values().stream(), subtasks.values().stream())
                .filter(t -> t.getId() != task.getId())
                .anyMatch(t -> Task.isOverlapping(task, t));
    }
}