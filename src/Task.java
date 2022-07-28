// Модификаторы доступа установил protected. Наставник на вебинаре говорил, что нужно делать как protected

public class Task {
    // Объявляем поля класса Task
    protected int id; // Уникальный идентификационный номер задачи, по которому её можно будет найти
    protected String name; // Название, кратко описывающее суть задачи (например, «Переезд»)
    protected String description; // Описание, в котором раскрываются детали
    protected String status; //Статус, отображающий её прогресс
    /* Мы будем выделять следующие этапы жизни задачи:
    NEW — задача только создана, но к её выполнению ещё не приступили.
    IN_PROGRESS — над задачей ведётся работа.
    DONE — задача выполнена */


    public Task(int id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
