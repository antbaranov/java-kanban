public class TaskManager {
    int id;
    HashMap<Integer, String> tasks;

    // Объявление конструктора
    public TaskManager(int id, HashMap<Integer, String> tasks) {
        this.id = id;
        this.tasks = tasks;
    }

    // Метод создания простой задачи
    public Task creatTask(String taskName, String taskDescription, String taskStatus) {
        HashMap<Integer, String> tasks;
        id++;
        Task task = new Task(id, taskName, taskDescription, taskStatus)
        tasks.put(taskId, task)
        return task;
    }

    // Метод создания подзадачи
    public SubTask creatSubTask(String epicName, String epicDescription, String epicStatus) { // параметры изменить
        HashMap<Integer, String> subTasks;
        id++;
        SubTask subTask = new SubTask(taskId, taskName, taskDescription, taskStatus)
        subTasks.put(id, subTask)
        return subTask;
    }

    // Метод создания Эпика
    public Epic creatEpic(String epicName, String epicDescription, String epicStatus) { // параметры изменить
        HashMap<Integer, String> epics;
        id++;
        Epic epic = new Epic(taskId, taskName, taskDescription, taskStatus)
        epics.put(id, epic)
        return epic;
    }


    // Метод обновления данных
    public void updateTask(Task, task) {
        task.put(task.getId(), task);
    }


} // скобка закрывает class TaskManager
