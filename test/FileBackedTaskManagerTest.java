package test;

import logic.FileBackedTaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoadEmptyFile() {
        Path filePath = tempDir.resolve("empty_tasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(filePath);

        // Проверяем, что файл пуст
        assertTrue(manager.getAllTasks().isEmpty());
        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());

        // Сохраняем пустой файл
        manager.save();

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(filePath);

        // Проверяем, что файл пуст
        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }

    @Test
    void testSaveAndLoadMultipleTasks() {
        Path filePath = tempDir.resolve("multiple_tasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(filePath);

        // Создаем задачи
        Task task1 = new Task("Task1", "Description1");
        Task task2 = new Task("Task2", "Description2");
        Epic epic1 = new Epic("Epic1", "DescriptionEpic1");
        Subtask subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", epic1.getId());

        // Добавляем задачи в менеджер
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);

        // Сохраняем задачи в файл
        manager.save();

        // Загружаем задачи из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(filePath);

        // Проверяем задачи
        assertEquals(2, loadedManager.getAllTasks().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtasks().size());

        Task loadedTask1 = loadedManager.getTaskById(task1.getId());
        Task loadedTask2 = loadedManager.getTaskById(task2.getId());
        Epic loadedEpic1 = loadedManager.getEpicById(epic1.getId());
        Subtask loadedSubtask1 = loadedManager.getSubtaskById(subtask1.getId());

        assertNotNull(loadedTask1);
        assertNotNull(loadedTask2);
        assertNotNull(loadedEpic1);
        assertNotNull(loadedSubtask1);

        assertEquals("Task1", loadedTask1.getName());
        assertEquals("Task2", loadedTask2.getName());
        assertEquals("Epic1", loadedEpic1.getName());
        assertEquals("Subtask1", loadedSubtask1.getName());
    }
}