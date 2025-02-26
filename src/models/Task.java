package models;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private models.TaskStatus status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = models.TaskStatus.NEW;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public models.TaskStatus getStatus() {
        return status;
    }

    public void setStatus(models.TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Задача{" +
                "название='" + name + '\'' +
                ", описание='" + description + '\'' +
                ", id=" + id +
                ", статус=" + status +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }
}