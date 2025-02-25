
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

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
        Task task = new Task(name, description, 0);
        taskManager.createTask(task);
        System.out.println("Задача добавлена с ID: " + task.getId());
    }

    private static void addEpic(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите название эпика:");
        String name = scanner.nextLine();
        System.out.println("Введите описание эпика:");
        String description = scanner.nextLine();
        Epic epic = new Epic(name, description, 0);
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
        scanner.nextLine(); // Consume newline
        Subtask subtask = new Subtask(name, description, 0, epicId);
        taskManager.createSubtask(subtask);
        System.out.println("Подзадача добавлена с ID: " + subtask.getId());
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
}