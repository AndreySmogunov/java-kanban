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
        this.filePath = filePath;
        loadFromFile();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        saveToFile();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        saveToFile();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        saveToFile();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        saveToFile();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        saveToFile();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        saveToFile();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        saveToFile();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        saveToFile();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        saveToFile();
    }

    private void saveToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();

            for (Task task : getAllTasks()) {
                writer.write(taskToString(task));
                writer.newLine();
            }

            for (Epic epic : getAllEpics()) {
                writer.write(taskToString(epic));
                writer.newLine();
            }

            for (Subtask subtask : getAllSubtasks()) {
                writer.write(taskToString(subtask));
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
                Task task = fromString(line);
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

    private String taskToString(Task task) {
        String type = task instanceof Epic ? "EPIC" : task instanceof Subtask ? "SUBTASK" : "TASK";
        String epicId = task instanceof Subtask ? String.valueOf(((Subtask) task).getEpicId()) : "";
        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                type,
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                epicId);
    }

    private Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];

        switch (type) {
            case "EPIC":
                return new Epic(id, name, description, status);
            case "SUBTASK":
                int epicId = Integer.parseInt(fields[5]);
                return new Subtask(id, name, description, status, epicId);
            default:
                return new Task(id, name, description, status);
        }
    }
}