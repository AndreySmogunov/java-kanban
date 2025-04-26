package test;

import logic.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    protected abstract T createTaskManager();

    @Test
    void testAddAndGetTask() {
        Task task = new Task("Task1", "Description1", Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.createTask(task);
        Task retrievedTask = taskManager.getTaskById(task.getId());
        assertNotNull(retrievedTask, "Задача не найдена.");
        assertEquals(task, retrievedTask, "Задачи не совпадают.");
    }

    @Test
    void testAddAndGetEpic() {
        Epic epic = new Epic("Epic1", "DescriptionEpic1");
        taskManager.createEpic(epic);
        Epic retrievedEpic = taskManager.getEpicById(epic.getId());
        assertNotNull(retrievedEpic, "Эпик не найден.");
        assertEquals(epic, retrievedEpic, "Эпики не совпадают.");
    }

    @Test
    void testAddAndGetSubtask() {
        Epic epic = new Epic("Epic1", "DescriptionEpic1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "DescriptionSubtask1", Duration.ofMinutes(30), LocalDateTime.now(), epic.getId());
        taskManager.createSubtask(subtask);
        Subtask retrievedSubtask = taskManager.getSubtaskById(subtask.getId());
        assertNotNull(retrievedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, retrievedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void testDeleteTask() {
        Task task = new Task("Task1", "Description1", Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.createTask(task);
        taskManager.deleteTaskById(task.getId());
        assertNull(taskManager.getTaskById(task.getId()), "Задача не удалена.");
    }

    @Test
    void testDeleteEpic() {
        Epic epic = new Epic("Epic1", "DescriptionEpic1");
        taskManager.createEpic(epic);
        taskManager.deleteEpicById(epic.getId());
        assertNull(taskManager.getEpicById(epic.getId()), "Эпик не удален.");
    }

    @Test
    void testDeleteSubtask() {
        Epic epic = new Epic("Epic1", "DescriptionEpic1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "DescriptionSubtask1", Duration.ofMinutes(30), LocalDateTime.now(), epic.getId());
        taskManager.createSubtask(subtask);
        taskManager.deleteSubtaskById(subtask.getId());
        assertNull(taskManager.getSubtaskById(subtask.getId()), "Подзадача не удалена.");
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task("Task1", "Description1", Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task2", "Description2", Duration.ofMinutes(60), LocalDateTime.now().plusHours(1));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertTrue(tasks.contains(task1), "Задача 1 не найдена.");
        assertTrue(tasks.contains(task2), "Задача 2 не найдена.");
    }

    @Test
    void testGetAllEpics() {
        Epic epic1 = new Epic("Epic1", "DescriptionEpic1");
        Epic epic2 = new Epic("Epic2", "DescriptionEpic2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertTrue(epics.contains(epic1), "Эпик 1 не найден.");
        assertTrue(epics.contains(epic2), "Эпик 2 не найден.");
    }

    @Test
    void testGetAllSubtasks() {
        Epic epic = new Epic("Epic1", "DescriptionEpic1");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", Duration.ofMinutes(30), LocalDateTime.now(), epic.getId());
        Subtask subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", Duration.ofMinutes(60), LocalDateTime.now().plusHours(1), epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
        assertTrue(subtasks.contains(subtask1), "Подзадача 1 не найдена.");
        assertTrue(subtasks.contains(subtask2), "Подзадача 2 не найдена.");
    }

    @Test
    void testEpicStatusCalculation() {
        Epic epic = new Epic("Epic1", "DescriptionEpic1");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", Duration.ofMinutes(30), LocalDateTime.now(), epic.getId());
        subtask1.setStatus(TaskStatus.NEW);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", Duration.ofMinutes(60), LocalDateTime.now().plusHours(1), epic.getId());
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.createSubtask(subtask2);

        epic.updateEpicFields(taskManager.getEpicSubtasks(epic.getId()));
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Неверный статус эпика.");

        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        epic.updateEpicFields(taskManager.getEpicSubtasks(epic.getId()));
        assertEquals(TaskStatus.DONE, epic.getStatus(), "Неверный статус эпика.");
    }

    @Test
    void testTaskOverlap() {
        Task task1 = new Task("Task1", "Description1", Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task2", "Description2", Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(15));
        assertTrue(Task.isOverlapping(task1, task2), "Задачи должны пересекаться.");

        Task task3 = new Task("Task3", "Description3", Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30));
        assertFalse(Task.isOverlapping(task1, task3), "Задачи не должны пересекаться.");
    }
}