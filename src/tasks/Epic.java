package tasks;

import constants.Status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();
    private Instant endTime;

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Epic(String name, String description, Status status)  {
        super(name,  description, status);

    }

    public Epic( String name, String description, Status status, Instant startTime, long duration) {
        super( name, description, status, startTime, duration);
        this.endTime = super.getEndTime();
    }

    public void addSubTask(int subTaskId) {

        this.subTaskIds.add(subTaskId);
    }

    public List<Integer> getSubtaskIds() {

        return subTaskIds;
    }

    public void setSubtaskIds(int id) {

        subTaskIds.add(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskIds=" + subTaskIds +
                ", endTime=" + endTime +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", taskType=" + taskType +
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
