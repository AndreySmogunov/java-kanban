package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import logic.TaskManager;
import models.Task;

import java.io.IOException;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            switch (method) {
                case "GET":
                    if (path.equals("/tasks")) {
                        List<Task> tasks = taskManager.getAllTasks();
                        sendText(exchange, gson.toJson(tasks), 200);
                    } else {
                        int id = Integer.parseInt(path.substring("/tasks/".length()));
                        Task task = taskManager.getTaskById(id);
                        sendText(exchange, gson.toJson(task), 200);
                    }
                    break;
                case "POST":
                    String body = readText(exchange);
                    Task task = gson.fromJson(body, Task.class);
                    taskManager.createTask(task);
                    sendText(exchange, gson.toJson(task), 201);
                    break;
                case "DELETE":
                    int id = Integer.parseInt(path.substring("/tasks/".length()));
                    taskManager.deleteTaskById(id);
                    sendText(exchange, "", 204);
                    break;
                default:
                    sendText(exchange, "Method Not Allowed", 405);
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }
    }
}