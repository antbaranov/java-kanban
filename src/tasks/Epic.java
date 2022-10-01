package tasks;

import constants.TaskStatus;
import constants.Types;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTaskIds;
    private Instant endTime;
    public Epic(String name, String description, TaskStatus status)  {
        super(name,  description, status, null, null);
        subTaskIds = new ArrayList<>();
    }

    public Epic(int id, Types taskType, String name, TaskStatus status, String description) {
        super(id, taskType, name, status, description, null, null);
        subTaskIds = new ArrayList<>();
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }



    public void addSubTask(int subTaskId) {

        this.subTaskIds.add(subTaskId);
    }

    public List<Integer> getSubTaskIds() {

        return subTaskIds;
    }

    public void setSubTaskIds(List<Integer> subTaskIds) {

        this.subTaskIds = subTaskIds;
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
        Epic epic = (Epic) o;
        return Objects.equals(subTaskIds, epic.subTaskIds) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskIds, endTime);
    }
}
