public class SubTask extends Task {
    public SubTask(int id, String name, String description, String status) {
        super(id, name, description, status);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
