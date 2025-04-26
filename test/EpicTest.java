import models.Epic;
import models.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void createEpic() {
        int id = 0;
        TaskStatus status = null;
        Epic epic = new Epic("Test Epic", "Test Description");
        assertEquals("Test Epic", epic.getName(), "Epic name should be set correctly.");
        assertEquals("Test Description", epic.getDescription(), "Epic description should be set correctly.");
    }

    @Test
    void addSubtaskId() {
        int id = 0;
        TaskStatus status = null;
        Epic epic = new Epic("Test Epic", "Test Description");
        epic.addSubtaskId(1);
        assertEquals(1, epic.getSubtaskIds().size(), "Subtask ID should be added to the epic.");
    }
}