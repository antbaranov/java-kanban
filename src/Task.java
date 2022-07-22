public class Task {
    // Объявляем поля класса Task
    int taskId; // Уникальный идентификационный номер задачи, по которому её можно будет найти
    String taskName; // Название, кратко описывающее суть задачи (например, «Переезд»)
    String taskDescription; // Описание, в котором раскрываются детали
    String taskStatus; //Статус, отображающий её прогресс
    /* Мы будем выделять следующие этапы жизни задачи:
    NEW — задача только создана, но к её выполнению ещё не приступили.
    IN_PROGRESS — над задачей ведётся работа.
    DONE — задача выполнена */
    boolean taskIsNew;
    boolean taskIsInProgress;
    boolean taskIsDone;

    public Task(int taskId, String taskName, String taskDescription, String taskStatus,
                /*boolean taskIsNew, boolean taskIsInProgress, boolean taskIsDone*/) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        /*this.taskIsNew = taskIsNew;
        this.taskIsInProgress = taskIsInProgress;
        this.taskIsDone = taskIsDone;*/
    }
}
