package tasks;

import constants.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIds;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subTaskIds = new ArrayList<>();
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
        return "tasks.Epic{" +
                "subTaskIds=" + subTaskIds +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
