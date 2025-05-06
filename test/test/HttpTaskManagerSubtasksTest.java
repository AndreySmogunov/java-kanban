package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import logic.InMemoryTaskManager;
import logic.TaskManager;
import main.HttpTaskServer;
import models.Subtask;
import models.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerSubtasksTest {
    private TaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson;
    private HttpClient client;

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        gson = HttpTaskServer.getGson();
        client = HttpClient.newHttpClient();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("Test Subtask", "Description", TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 1);
        String subtaskJson = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> subtasksFromManager = manager.getAllSubtasks();
        assertNotNull(subtasksFromManager, "Подзадачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Test Subtask", subtasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("Test Subtask", "Description", TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 1);
        manager.createSubtask(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subtask.getId()))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Subtask retrievedSubtask = gson.fromJson(response.body(), Subtask.class);
        assertEquals("Test Subtask", retrievedSubtask.getName());
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("Test Subtask", "Description", TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 1);
        manager.createSubtask(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subtask.getId()))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());

        assertEquals(0, manager.getAllSubtasks().size());
    }

    @Test
    public void testGetAllSubtasks() throws IOException, InterruptedException {
        Subtask subtask1 = new Subtask("Test Subtask 1", "Description 1", TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 1);
        Subtask subtask2 = new Subtask("Test Subtask 2", "Description 2", TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now(), 1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Subtask> subtasks = gson.fromJson(response.body(), new TypeToken<List<Subtask>>() {}.getType());
        assertEquals(2, subtasks.size());
    }
}