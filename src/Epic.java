import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Epic extends Task {
    private List<Integer> subtaskIds;

    public Epic(String name, String description, int id) {
        super(name, description, id);
        this.subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        this.subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        this.subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    public void updateStatus(HashMap<Integer, Subtask> subtaskMap) {
        if (subtaskIds.isEmpty()) {
            setStatus(TaskStatus.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;

        for (int subtaskId : subtaskIds) {
            Subtask subtask = subtaskMap.get(subtaskId);
            if (subtask == null) {
                continue;
            }
            TaskStatus status = subtask.getStatus();

            if (status != TaskStatus.DONE) {
                allDone = false;
            }
            if (status != TaskStatus.NEW) {
                allNew = false;
            }
        }

        if (allDone) {
            setStatus(TaskStatus.DONE);
        } else if (allNew) {
            setStatus(TaskStatus.NEW);
        } else {
            setStatus(TaskStatus.IN_PROGRESS);
        }
    }
    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
