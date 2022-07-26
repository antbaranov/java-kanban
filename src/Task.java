public class Task {
    // Объявляем поля класса Task
    int id; // Уникальный идентификационный номер задачи, по которому её можно будет найти
    String name; // Название, кратко описывающее суть задачи (например, «Переезд»)
    String description; // Описание, в котором раскрываются детали
    String status; //Статус, отображающий её прогресс
    /* Мы будем выделять следующие этапы жизни задачи:
    NEW — задача только создана, но к её выполнению ещё не приступили.
    IN_PROGRESS — над задачей ведётся работа.
    DONE — задача выполнена */

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Task(int id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }


}
