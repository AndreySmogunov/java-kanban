package main;

import logic.Managers;
import logic.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Создаем задачи с новыми полями duration и startTime
        Task task1 = new Task("Задача 1", "Описание задачи 1", Duration.ofMinutes(30), LocalDateTime.now(), null);
        taskManager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Duration.ofMinutes(60), LocalDateTime.now().plusHours(1), null);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 1", "Подзадача эпика 1", TaskStatus.NEW, Duration.ofMinutes(45), LocalDateTime.now().plusHours(2), epic1.getId());
        try {
            taskManager.createSubtask(subtask1);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании подзадачи: " + e.getMessage());
        }
        Subtask subtask2 = new Subtask("Подзадача 2", "Подзадача эпика 1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusHours(3), epic1.getId());

        try {
            taskManager.createSubtask(subtask2);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании подзадачи: " + e.getMessage());
        }

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Подзадача эпика 2", TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusHours(4), epic2.getId());
        try {
            taskManager.createSubtask(subtask3);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании подзадачи: " + e.getMessage());
        }

        System.out.println("--- История после первоначального создания ---");
        printAllTasks(taskManager);

        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1.getId());

        System.out.println("--- История после доступа к задачам ---");
        printAllTasks(taskManager);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getSubtaskById(subtask3.getId());
        System.out.println("--- История после дополнительных обращений (должно отображаться только 10) ---");
        printAllTasks(taskManager);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Добавить задачу");
            System.out.println("2. Добавить эпик");
            System.out.println("3. Добавить подзадачу");
            System.out.println("4. Удалить задачу");
            System.out.println("5. Удалить эпик");
            System.out.println("6. Удалить подзадачу");
            System.out.println("7. Показать все задачи");
            System.out.println("8. Показать все эпики");
            System.out.println("9. Показать все подзадачи");
            System.out.println("10. Выход");
            System.out.println("11. Показать историю просмотров");
            System.out.println("12. Обновить задачу");
            System.out.println("13. Обновить эпик");
            System.out.println("14. Обновить подзадачу");
            System.out.println("15. Изменить статус задачи");
            System.out.println("16. Показать задачи по приоритету");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addTask(scanner, taskManager);
                    break;
                case 2:
                    addEpic(scanner, taskManager);
                    break;
                case 3:
                    addSubtask(scanner, taskManager);
                    break;
                case 4:
                    deleteTask(scanner, taskManager);
                    break;
                case 5:
                    deleteEpic(scanner, taskManager);
                    break;
                case 6:
                    deleteSubtask(scanner, taskManager);
                    break;
                case 7:
                    showAllTasks(taskManager);
                    break;
                case 8:
                    showAllEpics(taskManager);
                    break;
                case 9:
                    showAllSubtasks(taskManager);
                    break;
                case 10:
                    System.out.println("Выход.");
                    scanner.close();
                    return;
                case 11:
                    showHistory(taskManager);
                    break;
                case 12:
                    updateTask(scanner, taskManager);
                    break;
                case 13:
                    updateEpic(scanner, taskManager);
                    break;
                case 14:
                    updateSubtask(scanner, taskManager);
                    break;
                case 15:
                    changeTaskStatus(scanner, taskManager);
                    break;
                case 16:
                    showPrioritizedTasks(taskManager);
                    break;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }

    private static void addTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите название задачи:");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        System.out.println("Введите продолжительность задачи в минутах:");
        int durationMinutes = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите дату и время начала задачи (формат: yyyy-MM-ddTHH:mm):");
        String startTimeStr = scanner.nextLine();
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
        Task task = new Task(name, description, Duration.ofMinutes(durationMinutes), startTime, null);
        taskManager.createTask(task);
        System.out.println("Задача добавлена с ID: " + task.getId());
    }

    private static void addEpic(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите название эпика:");
        String name = scanner.nextLine();
        System.out.println("Введите описание эпика:");
        String description = scanner.nextLine();
        Epic epic = new Epic(name, description);
        taskManager.createEpic(epic);
        System.out.println("Эпик добавлен с ID: " + epic.getId());
    }

    private static void addSubtask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите название подзадачи:");
        String name = scanner.nextLine();
        System.out.println("Введите описание подзадачи:");
        String description = scanner.nextLine();
        System.out.println("Введите продолжительность подзадачи в минутах:");
        int durationMinutes = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите дату и время начала подзадачи (формат: yyyy-MM-ddTHH:mm):");
        String startTimeStr = scanner.nextLine();
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
        System.out.println("Введите ID эпика, к которому относится подзадача:");
        int epicId = scanner.nextInt();
        scanner.nextLine();
        try {
            Subtask subtask = new Subtask(name, description, TaskStatus.NEW, Duration.ofMinutes(durationMinutes), startTime, epicId);
            taskManager.createSubtask(subtask);
            System.out.println("Подзадача добавлена с ID: " + subtask.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании подзадачи: " + e.getMessage());
        }
    }

    private static void deleteTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите ID задачи для удаления:");
        int id = scanner.nextInt();
        scanner.nextLine();
        taskManager.deleteTaskById(id);
        System.out.println("Задача удалена.");
    }

    private static void deleteEpic(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите ID эпика для удаления:");
        int id = scanner.nextInt();
        scanner.nextLine();
        taskManager.deleteEpicById(id);
        System.out.println("Эпик удален.");
    }

    private static void deleteSubtask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите ID подзадачи для удаления:");
        int id = scanner.nextInt();
        scanner.nextLine();
        taskManager.deleteSubtaskById(id);
        System.out.println("Подзадача удалена.");
    }

    private static void showAllTasks(TaskManager taskManager) {
        System.out.println("Все задачи:");
        taskManager.getAllTasks().forEach(System.out::println);
    }

    private static void showAllEpics(TaskManager taskManager) {
        System.out.println("Все эпики:");
        taskManager.getAllEpics().forEach(System.out::println);
    }

    private static void showAllSubtasks(TaskManager taskManager) {
        System.out.println("Все подзадачи:");
        taskManager.getAllSubtasks().forEach(System.out::println);
    }

    private static void showHistory(TaskManager taskManager) {
        System.out.println("История просмотров:");
        taskManager.getHistory().forEach(System.out::println);
    }

    private static void updateTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите ID задачи для обновления:");
        int id = scanner.nextInt();
        scanner.nextLine();
        Task task = taskManager.getTaskById(id);
        if (task != null) {
            System.out.println("Введите новое название задачи:");
            String name = scanner.nextLine();
            System.out.println("Введите новое описание задачи:");
            String description = scanner.nextLine();
            System.out.println("Введите новую продолжительность задачи в минутах:");
            int durationMinutes = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Введите новое дату и время начала задачи (формат: yyyy-MM-ddTHH:mm):");
            String startTimeStr = scanner.nextLine();
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
            task.setName(name);
            task.setDescription(description);
            task.setDuration(Duration.ofMinutes(durationMinutes));
            task.setStartTime(startTime);
            taskManager.updateTask(task);
            System.out.println("Задача обновлена.");
        } else {
            System.out.println("Задача с таким ID не найдена.");
        }
    }

    private static void updateEpic(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите ID эпика для обновления:");
        int id = scanner.nextInt();
        scanner.nextLine();
        Epic epic = taskManager.getEpicById(id);
        if (epic != null) {
            System.out.println("Введите новое название эпика:");
            String name = scanner.nextLine();
            System.out.println("Введите новое описание эпика:");
            String description = scanner.nextLine();
            epic.setName(name);
            epic.setDescription(description);
            taskManager.updateEpic(epic);
            System.out.println("Эпик с ID " + epic.getId() + " обновлен.");
        } else {
            System.out.println("Эпик с таким ID не найден.");
        }
    }

    private static void updateSubtask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите ID подзадачи для обновления:");
        int id = scanner.nextInt();
        scanner.nextLine();
        Subtask subtask = taskManager.getSubtaskById(id);
        if (subtask != null) {
            System.out.println("Введите новое название подзадачи:");
            String name = scanner.nextLine();
            System.out.println("Введите новое описание подзадачи:");
            String description = scanner.nextLine();
            System.out.println("Введите новую продолжительность подзадачи в минутах:");
            int durationMinutes = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Введите новое дату и время начала подзадачи (формат: yyyy-MM-ddTHH:mm):");
            String startTimeStr = scanner.nextLine();
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
            subtask.setName(name);
            subtask.setDescription(description);
            subtask.setDuration(Duration.ofMinutes(durationMinutes));
            subtask.setStartTime(startTime);
            taskManager.updateSubtask(subtask);
            System.out.println("Подзадача обновлена.");
        } else {
            System.out.println("Подзадача с таким ID не найдена.");
        }
    }

    private static void changeTaskStatus(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите ID задачи для изменения статуса:");
        int id = scanner.nextInt();
        scanner.nextLine();
        Task task = taskManager.getTaskById(id);
        if (task != null) {
            System.out.println("Введите новый статус задачи (NEW, IN_PROGRESS, DONE):");
            String statusStr = scanner.nextLine();
            TaskStatus status = TaskStatus.valueOf(statusStr);
            task.setStatus(status);
            taskManager.updateTask(task);
            System.out.println("Статус задачи обновлен.");
        } else {
            System.out.println("Задача с таким ID не найдена.");
        }
    }

    private static void showPrioritizedTasks(TaskManager taskManager) {
        System.out.println("Задачи по приоритету:");
        taskManager.getPrioritizedTasks().forEach(System.out::println);
    }
}