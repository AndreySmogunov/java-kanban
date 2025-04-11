package test;

import models.Epic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void createEpic() {
        Epic epic = new Epic("Test Epic", "Test Description");
        assertEquals("Test Epic", epic.getName(), "Epic name should be set correctly.");
        assertEquals("Test Description", epic.getDescription(), "Epic description should be set correctly.");
    }

    @Test
    void addSubtaskId() {
        Epic epic = new Epic("Test Epic", "Test Description");
        epic.addSubtaskId(1);
        assertEquals(1, epic.getSubtaskIds().size(), "Subtask ID should be added to the epic.");
    }
}