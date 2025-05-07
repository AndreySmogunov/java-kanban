package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import logic.TaskManager;
import models.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();

            if (method.equals("GET")) {
                List<Task> history = taskManager.getHistory();
                sendText(exchange, gson.toJson(history), 200);
            } else {
                sendText(exchange, "Method Not Allowed", 405);
            }
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }
    }
}