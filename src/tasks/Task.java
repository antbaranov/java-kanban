package tasks;

import constants.Status;
import constants.TaskType;

public class Task {
    // Объявляем поля класса tasks.Task
    protected int id; // Уникальный идентификационный номер задачи, по которому её можно будет найти
    protected String name; // Название, кратко описывающее суть задачи (например, «Переезд»)
    protected String description; // Описание, в котором раскрываются детали
    protected Status status; //Статус, отображающий её прогресс


    /* Мы будем выделять следующие этапы жизни задачи:
            NEW — задача только создана, но к её выполнению ещё не приступили.
            IN_PROGRESS — над задачей ведётся работа.
            DONE — задача выполнена */
    protected TaskType taskType; // Тип задачи


    public Task(String name, String description, Status status) {
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

    public Status getStatus() {
        return status;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "tasks.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
