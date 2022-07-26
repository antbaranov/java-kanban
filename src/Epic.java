public class Epic extends Task {
    String epicName;


    public Epic(int id, String name, String description, String status, String epicName) {
        super(id, name, description, status);
        this.epicName = epicName;
    }
}
