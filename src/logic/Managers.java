package logic;

import java.nio.file.Path;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    public static TaskManager getFileBackedTaskManager(Path filePath) {
        return new FileBackedTaskManager(filePath);
    }
}