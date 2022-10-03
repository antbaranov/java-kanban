package tasks;

import constants.TaskStatus;
import constants.Types;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class Task {
    // Объявляем поля класса tasks.Task
    protected int id; // Уникальный идентификационный номер задачи, по которому её можно будет найти
    protected Types taskType; // Тип задачи
    protected String name; // Название, кратко описывающее суть задачи (например, «Переезд»)
    protected String description; // Описание, в котором раскрываются детали
    protected TaskStatus status; //Статус, отображающий её прогресс

    LocalDateTime startTime;
    Duration duration;
    LocalDateTime endTime;

    public Task(
            String name,
            TaskStatus status,
            String description,
            LocalDateTime startTime,
            Duration duration
    ) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(
                int id,
                Types taskType,
                String name,
                TaskStatus status,
                String description,
                LocalDateTime startTime,
                Duration duration
    ) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, TaskStatus aNew, Instant now, int i) {
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

    public TaskStatus getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return id + "," +
                Types.TASK + "," +
                name + "," +
                description + "," +
                startTime + "," +
                duration;
    }
}
