import java.util.HashMap;

public class TaskManager {
    int id;

    // Метод создания простой задачи
    HashMap<Integer, Task> tasks = new HashMap<>();
    public Task creatTask(String name, String description, String status) {
        id++;
        Task task = new Task(id, name, description, status);
        tasks.put(id, task);
        return task;
    }

    // Метод создания подзадачи
    HashMap<Integer, Task> subTasks = new HashMap<>();
    public Task creatSubTask(String name, String description, String status) {
        id++;
        Task subTask = new Task(id, name, description, status);
        subTasks.put(id, subTask);
        return subTask;
    }


    // Метод обновления данных
    /*public void updateTask(Task, task) {
        task.put(task.getId(), task);
    }*/


} // скобка закрывает class TaskManager
