public class Epic extends Task {
    int epicId;
    String epicName;

    public Epic(int id, String name, String description, String status, int epicId, String epicName) {
        super(id, name, description, status);
        this.epicId = epicId;
        this.epicName = epicName;
    }
}
}
