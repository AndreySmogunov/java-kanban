package main;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import logic.Managers;
import logic.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Gson gson = new Gson();
    private static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(taskManager, gson));
        server.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
        server.createContext("/epics", new EpicHandler(taskManager, gson));
        server.createContext("/history", new HistoryHandler(taskManager, gson));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
        server.start();
        System.out.println("HTTP server started on port " + PORT);
    }
}