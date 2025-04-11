package test;

import logic.FileBackedTaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

public class FileBackedTaskManagerTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoadEmptyFile() {
        Path filePath = tempDir.resolve("empty_tasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(filePath);

        // Проверяем, что файл пуст
        Assertions.assertTrue(manager.getAllTasks().isEmpty());
        Assertions.assertTrue(manager.getAllEpics().isEmpty());
        Assertions.assertTrue(manager.getAllSubtasks().isEmpty());

        // Сохраняем пустой файл
        manager.save();

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(filePath);

        // Проверяем, что файл пуст
        Assertions.assertTrue(loadedManager.getAllTasks().isEmpty());
        Assertions.assertTrue(loadedManager.getAllEpics().isEmpty());
        Assertions.assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }


}