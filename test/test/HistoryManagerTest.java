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
        Task task1 = new Task("Task1", "Description1", Duration.ofMinutes(30), LocalDateTime.now(), null);
        Task task2 = new Task("Task2", "Description2", Duration.ofMinutes(60), LocalDateTime.now().plusHours(1), null);

        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Неверное количество задач в истории.");
        assertTrue(history.contains(task1), "Задача 1 не найдена в истории.");
        assertTrue(history.contains(task2), "Задача 2 не найдена в истории.");
    }

    @Test
    void testEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой.");
    }
}