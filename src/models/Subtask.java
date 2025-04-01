package models;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Подзадача{" +
                "название='" + getName() + '\'' +
                ", описание='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", статус=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}