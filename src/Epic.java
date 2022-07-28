import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTaskIds; // если сделать приватным, тогда не работает в классе Manager

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
        subTaskIds = new ArrayList<>();
    }

    public void addSubTask(int subTaskId) {

        this.subTaskIds.add(subTaskId);
    }

    public ArrayList<Integer> getSubTaskIds() {

        return subTaskIds;
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {

        this.subTaskIds = subTaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskIds=" + subTaskIds +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
