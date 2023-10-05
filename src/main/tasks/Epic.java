package tasks;

import com.sun.jdi.Type;
import constants.Status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subTaskIds = new ArrayList<>();
    private Instant endTime;

    public Epic(String name, String description, Status status) {
        super(name, description, status);

    }

    public Epic(String name, String description, Status status, Instant startTime, long duration) {
        super(name, description, status, startTime, duration);
        this.endTime = super.getEndTime();
    }

    public Epic(Type taskType, String name, String description, Status status, Instant startTime, long duration) {
        super(taskType, name, description, status, startTime, duration);
        this.endTime = super.getEndTime();
    }

    public List<Integer> getSubtaskIds() {

        return subTaskIds;
    }

    public void setSubtaskIds(int id) {

        subTaskIds.add(id);
    }


    @Override
    public Instant getEndTime() {

        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subTaskIds +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", startTime='" + getStartTime().toEpochMilli() + '\'' +
                ", endTime='" + getEndTime().toEpochMilli() + '\'' +
                ", duration='" + getDuration() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskIds, epic.subTaskIds) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskIds, endTime);
    }
}