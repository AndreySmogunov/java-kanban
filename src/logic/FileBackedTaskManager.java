// src/main/java/logic/FileBackedTaskManager.java
package logic;

import exceptions.ManagerSaveException;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path filePath;

    public FileBackedTaskManager(Path filePath) {
        super(new InMemoryHistoryManager());
        this.filePath = filePath;
        loadFromFile();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        updateEpicFields(subtask.getEpicId());
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        updateEpicFields(subtask.getEpicId());
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = getSubtaskById(id);
        if (subtask != null) {
            super.deleteSubtaskById(id);
            updateEpicFields(subtask.getEpicId());
            save();
        }
    }

    private void updateEpicFields(int epicId) {
        Epic epic = getEpicById(epicId);
        if (epic != null) {
            List<Subtask> subtasks = getEpicSubtasks(epicId);
            epic.updateEpicFields(subtasks);
        }
    }

    public void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("id,type,name,status,description,duration,startTime,epic");
            writer.newLine();

            for (Task task : getAllTasks()) {
                writer.write(task.toString());
                writer.newLine();
            }

            for (Epic epic : getAllEpics()) {
                writer.write(epic.toString());
                writer.newLine();
            }

            for (Subtask subtask : getAllSubtasks()) {
                writer.write(subtask.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач в файл", e);
        }
    }

    private void loadFromFile() {
        if (!Files.exists(filePath)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine(); // Пропускаем заголовок

            String line;
            while ((line = reader.readLine()) != null) {
                Task task = Task.fromString(line);
                if (task instanceof Epic) {
                    super.createEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    super.createSubtask((Subtask) task);
                } else {
                    super.createTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке задач из файла", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(Path filePath) {
        FileBackedTaskManager manager = new FileBackedTaskManager(filePath);
        manager.loadFromFile();
        return manager;
    }
}