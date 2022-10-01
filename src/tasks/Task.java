package tasks;

import constants.TaskStatus;
import constants.Types;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    // Объявляем поля класса tasks.Task
    protected int id; // Уникальный идентификационный номер задачи, по которому её можно будет найти
    protected String name; // Название, кратко описывающее суть задачи (например, «Переезд»)
    protected String description; // Описание, в котором раскрываются детали
    protected TaskStatus status; //Статус, отображающий её прогресс
    protected Types taskType; // Тип задачи
    Duration duration;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, Types taskType, String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.taskType = taskType;
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

    public TaskStatus getStatus() {
        return status;
    }

    public Types getTaskType() {
        return taskType;
    }

    public void setTaskType(Types taskType) {
        this.taskType = taskType;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Instant getEndTime() {
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
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", taskType=" + taskType +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) &&
                status == task.status && taskType == task.taskType && Objects.equals(duration, task.duration) &&
                Objects.equals(startTime, task.startTime) && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, taskType, duration, startTime, endTime);
    }
}
