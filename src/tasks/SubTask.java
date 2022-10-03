package tasks;

import constants.TaskStatus;
import constants.Types;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask(
            String name,
            TaskStatus status,
            String description,
            int epicId,
            LocalDateTime startTime,
            Duration duration
    ) {
        super(name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(
            int id,
            Types taskType,
            String name,
            TaskStatus status,
            String description,
            int epicId,
            LocalDateTime startTime,
            Duration duration
    ) {
        super(id, taskType, name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, TaskStatus aNew, int epicId, Instant now, int i) {
        super(title, description, aNew, now, i);
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
        return "SubTask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", taskType=" + taskType +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

}
