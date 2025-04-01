package main;

import logic.Managers;
import logic.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;

import java.util.List;
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

        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 1", "Подзадача эпика 1", epic1.getId());
        try {
            taskManager.createSubtask(subtask1);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании подзадачи: " + e.getMessage());
        }
        Subtask subtask2 = new Subtask("Подзадача 2", "Подзадача эпика 1", epic1.getId());

        try {
            taskManager.createSubtask(subtask2);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании подзадачи: " + e.getMessage());
        }


        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Подзадача эпика 2", epic2.getId());
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
                    deleteTask(scanner, taskManager, scanner);
                    break;
                case 5:
                    deleteEpic(scanner, taskManager, scanner);
                    break;
                case 6:
                    deleteSubtask(scanner, taskManager, scanner);
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
                    return;
                case 11:
                    showHistory(taskManager);
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
        Task task = new Task(name, description);
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
        System.out.println("Введите ID эпика, к которому относится подзадача:");
        int epicId = scanner.nextInt();
        scanner.nextLine();
        try {
            Subtask subtask = new Subtask(name, description, epicId);
            taskManager.createSubtask(subtask);
            System.out.println("Подзадача добавлена с ID: " + subtask.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании подзадачи: " + e.getMessage());
        }
    }

    private static void deleteTask(Scanner scanner, TaskManager taskManager, Scanner scanner1) {
        System.out.println("Введите ID задачи для удаления:");
        int id = scanner.nextInt();
        scanner.nextLine();
        taskManager.deleteTaskById(id);
        System.out.println("Задача удалена.");
    }

    private static void deleteEpic(Scanner scanner, TaskManager taskManager, Scanner scanner1) {
        System.out.println("Введите ID эпика для удаления:");
        int id = scanner.nextInt();
        scanner.nextLine();
        taskManager.deleteEpicById(id);
        System.out.println("Эпик удален.");
    }

    private static void deleteSubtask(Scanner scanner, TaskManager taskManager, Scanner scanner1) {
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
        List<Task> history = taskManager.getHistory();
        if (history.isEmpty()) {
            System.out.println("История просмотров пуста.");
        } else {
            history.forEach(System.out::println);
        }
    }
}