// src/test/java/test/InMemoryTaskManagerTest.java
package test;

import logic.HistoryManager;
import logic.InMemoryTaskManager;
import logic.TaskManager;
import models.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @Test
    void testSpecificInMemoryTaskManagerMethod() {
        // Добавьте специфические тесты для InMemoryTaskManager, если есть
    }

    private record InMemoryHistoryManager() implements HistoryManager {
        @Override
        public void add(Task task) {

        }

        @Override
        public void remove(int id) {

        }

        @Override
        public List<Task> getHistory() {
            return List.of();
        }
    }
}