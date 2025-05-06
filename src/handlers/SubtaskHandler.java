package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import logic.TaskManager;
import models.Subtask;

import java.io.IOException;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            switch (method) {
                case "GET":
                    if (path.equals("/subtasks")) {
                        List<Subtask> subtasks = taskManager.getAllSubtasks();
                        sendText(exchange, gson.toJson(subtasks), 200);
                    } else {
                        int id = Integer.parseInt(path.substring("/subtasks/".length()));
                        Subtask subtask = taskManager.getSubtaskById(id);
                        sendText(exchange, gson.toJson(subtask), 200);
                    }
                    break;
                case "POST":
                    String body = readText(exchange);
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    taskManager.createSubtask(subtask);
                    sendText(exchange, gson.toJson(subtask), 201);
                    break;
                case "DELETE":
                    int id = Integer.parseInt(path.substring("/subtasks/".length()));
                    taskManager.deleteSubtaskById(id);
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