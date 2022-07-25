public class Epic extends Task {
    int epicId;
    String epicName;

    public Epic(int taskId, String taskName, String taskDescription, String taskStatus, int epicId, String epicName) {
        super(taskId, taskName, taskDescription, taskStatus);
        this.epicId = epicId;
        this.epicName = epicName;
    }
}
