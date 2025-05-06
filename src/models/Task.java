package models;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s", id, "TASK", name, status, description, duration.toMinutes(), startTime);
    }

    public static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(fields[5]));
        LocalDateTime startTime = LocalDateTime.parse(fields[6]);

        switch (type) {
            case "EPIC":
                return new Epic(id, name, description, status, duration, startTime);
            case "SUBTASK":
                int epicId = Integer.parseInt(fields[7]);
                return new Subtask(id, name, description, status, duration, startTime, epicId);
            default:
                return new Task(id, name, description, status, duration, startTime);
        }
    }

    public static boolean isOverlapping(Task task1, Task task2) {
        if (task1.getStartTime() == null || task1.getEndTime() == null || task2.getStartTime() == null || task2.getEndTime() == null) {
            return false;
        }
        return task1.getStartTime().isBefore(task2.getEndTime()) && task2.getStartTime().isBefore(task1.getEndTime());
    }
}