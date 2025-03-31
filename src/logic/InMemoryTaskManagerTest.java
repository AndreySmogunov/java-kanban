package logic;

import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
    }

    @Test
    void tasksAreEqualIfIdsAreEqual() {
        Task task1 = new Task("Task 1", "Desc 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Desc 2");
        task2.setId(1);

        assertEquals(task1, task2, "Tasks with the same ID should be equal.");
    }

    @Test
    void subtasksAreEqualIfIdsAreEqual() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Desc 1", epic.getId());
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Subtask 2", "Desc 2", epic.getId());
        subtask2.setId(1);

        assertEquals(subtask1, subtask2, "Subtasks with the same ID should be equal.");
    }

    @Test
    void epicCannotBeAddedToItselfAsSubtask() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        taskManager.createEpic(epic);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Subtask subtask = new Subtask("Subtask 1", "Description", epic.getId());
            // Code that should throw the exception if epic ID equal subtask id.
        });

        //  assertEquals("Epic cannot be a subtask of itself.", exception.getMessage());
    }

    @Test
    void subtaskCannotBeItsOwnEpic() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description", epic.getId());

        //  IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> subtask.setEpicId(subtask.getId()));
        //  assertEquals("Subtask cannot be its own epic.", exception.getMessage());
    }

    @Test
    void managersAlwaysReturnInitializedInstances() {
        TaskManager defaultManager = Managers.getDefault();
        assertNotNull(defaultManager, "getDefault() should return an initialized TaskManager.");
        HistoryManager defaultHistory = Managers.getDefaultHistory();
        assertNotNull(defaultHistory, "getDefaultHistory() should return an initialized HistoryManager.");
    }

    @Test
    void inMemoryTaskManagerAddsDifferentTaskTypesAndFindsById() {
        Task task = new Task("Task 1", "Task Description");
        taskManager.createTask(task);
        Epic epic = new Epic("Epic 1", "Epic Description");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", epic.getId());
        taskManager.createSubtask(subtask);

        assertNotNull(taskManager.getTaskById(task.getId()), "Task should be found by ID.");
        assertNotNull(taskManager.getEpicById(epic.getId()), "Epic should be found by ID.");
        assertNotNull(taskManager.getSubtaskById(subtask.getId()), "Subtask should be found by ID.");
    }

    @Test
    void tasksWithGivenIdAndGeneratedIdDoNotConflict() {
        Task taskWithGivenId = new Task("Task 1", "Description");
        taskWithGivenId.setId(1); // Manually set an ID

        Task taskWithGeneratedId = new Task("Task 2", "Description");
        taskManager.createTask(taskWithGeneratedId);
        // Check for different id.
        assertNotEquals(taskWithGivenId.getId(),taskWithGeneratedId.getId());
    }

    @Test
    void taskIsImmutableAfterAddingToManager() {
        Task task = new Task("Original Task", "Original Description");
        taskManager.createTask(task);
        int originalId = task.getId();
        TaskStatus originalStatus = task.getStatus();

        task.setName("Modified Task");
        task.setDescription("Modified Description");
        task.setStatus(TaskStatus.DONE);

        Task retrievedTask = taskManager.getTaskById(originalId);

        assertNotEquals("Modified Task", retrievedTask.getName(), "Task name should not be modified in the manager.");
        assertNotEquals("Modified Description", retrievedTask.getDescription(), "Task description should not be modified in the manager.");
        assertEquals(originalStatus, retrievedTask.getStatus(), "Task status should not be modified in the manager.");
    }

    @Test
    void tasksAddedToHistoryManagerPreservePreviousData() {
        Task task = new Task("Task 1", "Description");
        taskManager.createTask(task);
        int id = task.getId();

        // First access, record initial state
        Task accessedTask1 = taskManager.getTaskById(id);
        assertEquals("Task 1", accessedTask1.getName());

        //Modify The Task.
        task.setName("Modified Name");
        taskManager.updateTask(task);

        Task accessedTask2 = taskManager.getTaskById(id);
        assertEquals("Modified Name", accessedTask2.getName());

        List<Task> history = taskManager.getHistory();
        assertEquals("Task 1", history.get(0).getName(), "History should contain the task's original data");
        assertEquals("Modified Name", history.get(1).getName(), "History should contain the task's modified data");
    }
}