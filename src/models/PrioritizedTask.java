package models;

import java.time.LocalDateTime;

public class PrioritizedTask implements Comparable<PrioritizedTask> {
    private final Task task;
    private final LocalDateTime startTime;

    public PrioritizedTask(Task task) {
        this.task = task;
        this.startTime = task.getStartTime();
    }

    public Task getTask() {
        return task;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public int compareTo(PrioritizedTask other) {
        if (this.startTime == null && other.startTime == null) {
            return 0;
        }
        if (this.startTime == null) {
            return 1;
        }
        if (other.startTime == null) {
            return -1;
        }
        return this.startTime.compareTo(other.startTime);
    }
}