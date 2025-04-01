package models;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        this.subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        this.subtaskIds.remove(subtaskId);
    }

    @Override
    public String toString() {
        return "Эпик{" +
                "название='" + getName() + '\'' +
                ", описание='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", статус=" + getStatus() +
                ", подзадача=" + subtaskIds +
                '}';
    }
}