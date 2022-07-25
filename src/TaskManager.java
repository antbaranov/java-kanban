public class TaskManager {
    int taskId;
    HashMap<Integer, String> tasks;

    // Объявление конструктора
    public TaskManager(int taskId, HashMap<Integer, String> tasks) {
        this.taskId = taskId;
        this.tasks = tasks;
    }

    // Метод создания всех типов задач
    public Task creatTask(String taskName, String taskDescription, String taskStatus) {
        taskId++;
        Task task = new Task(taskId, taskName, taskDescription, taskStatus)
        tasks.put(taskId, task)
        return task;
    }

    // Метод создания Эпика
    public Epic creatEpic(String taskName, String taskDescription, String taskStatus) {
        taskId++;
        Epic epic = new Epic(taskId, taskName, taskDescription, taskStatus)
        tasks.put(taskId, task)
        return task;
    }




} // скобка закрывает class TaskManager
