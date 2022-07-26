import java.util.HashMap;

public class TaskManager {
    int id;

    // Метод создания простой задачи task
    HashMap<Integer, Task> tasks = new HashMap<>();
    public Task creatTask(String name, String description, String status) {
        id++;
        Task task = new Task(id, name, description, status);
        tasks.put(id, task);
        return task;
    }

    // Метод создания подзадачи subTask
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public SubTask creatSubTask(String name, String description, String status) {
        id++;
        SubTask subTask = new SubTask(id, name, description, status);
        subTasks.put(id, subTask);
        return subTask;
    }

    // Метод создания Эпика Epic
    HashMap<Integer, Epic> epics = new HashMap<>();
    public Epic creatEpic(String name, String description, String status) {
        id++;
        Epic epic = new Epic(id, name, description, status);
        epics.put(id, epic);
        return epic;
    }


    // Метод обновления данных
    /*public void updateTask(Task, task) {
        task.put(task.getId(), task);
    }*/


} // скобка закрывает class TaskManager
