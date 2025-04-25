package test;

import logic.HistoryManager;
import logic.InMemoryHistoryManager;
import models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testAddAndGetHistory() {
        Task task1 = new Task("Task1", "Description1", Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task2", "Description2", Duration.ofMinutes(60), LocalDateTime.now().plusHours(1));

        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Неверное количество задач в истории.");
        assertTrue(history.contains(task1), "Задача 1 не найдена в истории.");
        assertTrue(history.contains(task2), "Задача 2 не найдена в истории.");
    }

    @Test
    void testRemoveFromHistory() {
        Task task1 = new Task("Task1", "Description1", Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task2", "Description2", Duration.ofMinutes(60), LocalDateTime.now().plusHours(1));
        Task task3 = new Task("Task3", "Description3", Duration.ofMinutes(90), LocalDateTime.now().plusHours(2));

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Неверное количество задач в истории.");
        assertTrue(history.contains(task1), "Задача 1 не найдена в истории.");
        assertTrue(history.contains(task3), "Задача 3 не найдена в истории.");
    }

    @Test
    void testDuplicateTasksInHistory() {
        Task task1 = new Task("Task1", "Description1", Duration.ofMinutes(30), LocalDateTime.now());

        historyManager.add(task1);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Дублирующиеся задачи не должны добавляться в историю.");
    }

    @Test
    void testEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой.");
    }
}