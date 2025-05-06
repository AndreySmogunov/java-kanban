package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import logic.TaskManager;
import models.Epic;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            switch (method) {
                case "GET":
                    if (path.equals("/epics")) {
                        List<Epic> epics = taskManager.getAllEpics();
                        sendText(exchange, gson.toJson(epics), 200);
                    } else {
                        int id = Integer.parseInt(path.substring("/epics/".length()));
                        Epic epic = taskManager.getEpicById(id);
                        sendText(exchange, gson.toJson(epic), 200);
                    }
                    break;
                case "POST":
                    String body = readText(exchange);
                    Epic epic = gson.fromJson(body, Epic.class);
                    taskManager.createEpic(epic);
                    sendText(exchange, gson.toJson(epic), 201);
                    break;
                case "DELETE":
                    int id = Integer.parseInt(path.substring("/epics/".length()));
                    taskManager.deleteEpicById(id);
                    sendText(exchange, "", 204);
                    break;
                default:
                    sendText(exchange, "Method Not Allowed", 405);
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }
    }
}
