package test;

import logic.InMemoryHistoryManager;
import logic.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @Test
    void testSpecificInMemoryTaskManagerMethod() {
    }
}