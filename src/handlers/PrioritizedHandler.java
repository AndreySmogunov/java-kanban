package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import logic.TaskManager;
import models.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();

            if (method.equals("GET")) {
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                sendText(exchange, gson.toJson(prioritizedTasks), 200);
            } else {
                sendText(exchange, "Method Not Allowed", 405);
            }
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }
    }
}