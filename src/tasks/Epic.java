package tasks;

import constants.TaskStatus;
import constants.Types;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTaskId;
    private Instant endTime;
    public Epic(String name, String description, TaskStatus status)  {
        super(name, status,  description,  null, null);
        subTaskId = new ArrayList<>();
    }

    public Epic(int id, Types taskType, String name, TaskStatus status, String description) {
        super(id, taskType, name, status, description, null, null);
        subTaskId = new ArrayList<>();
    }

    public Epic(String title, String description, TaskStatus aNew, Instant now, int i) {
        super(title, description, aNew, now, i);
    }

    public void addSubTask(int subTaskId) {

        this.subTaskId.add(subTaskId);
    }

    public List<Integer> getSubTaskId() {

        return subTaskId;
    }

    public void setSubTaskId(List<Integer> subTaskId) {

        this.subTaskId = subTaskId;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "subTaskIds=" + subTaskId +
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
        return Objects.equals(subTaskId, epic.subTaskId) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskId, endTime);
    }

    public void setStatus(TaskStatus aNew) {
    }
}
