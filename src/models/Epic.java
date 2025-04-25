package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskIds;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, Duration.ZERO, null);
        this.subtaskIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.setId(id);
        this.setStatus(status);
        this.subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void updateEpicFields(List<Subtask> subtasks) {
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = null;

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasks) {
            this.duration = this.duration.plus(subtask.getDuration());
            if (this.startTime == null || subtask.getStartTime().isBefore(this.startTime)) {
                this.startTime = subtask.getStartTime();
            }
            if (this.endTime == null || subtask.getEndTime().isAfter(this.endTime)) {
                this.endTime = subtask.getEndTime();
            }

            if (subtask.getStatus() != TaskStatus.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != TaskStatus.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            this.setStatus(TaskStatus.NEW);
        } else if (allDone) {
            this.setStatus(TaskStatus.DONE);
        } else {
            this.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s", getId(), "EPIC", getName(), getStatus(), getDescription(), duration.toMinutes(), startTime);
    }
}