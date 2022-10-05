package tasks;

import com.sun.jdi.Type;
import constants.Status;

import java.time.Instant;
import java.util.Objects;

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
    protected Type taskType; // Тип задачи
    private Instant startTime;
    private long duration;

    public Task(String name, String description, Status status, Instant startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Instant getEndTime() {
        long SECONDS_IN_MINUTE = 60L;
        return startTime.plusSeconds(duration * SECONDS_IN_MINUTE);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
               // ", taskType=" + taskType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && taskType == task.taskType && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskType, name, description, status, startTime, duration);
    }
}
