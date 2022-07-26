import java.util.HashMap;

public class TaskManager {





    // Метод создания простой задачи
    HashMap<Integer, Task> tasks;
    public Task creatTask(String name, String description, String status) {
        id++;
        Task task = new Task(id, name, description, status);
        tasks.put(id, task);
        return task;
    }




/*

    // Метод создания подзадачи
    public SubTask creatSubTask(String subTaskName, String subTaskDescription, String subTaskStatus) { // параметры изменить
        HashMap<Integer, String> subTasks;
        id++;
        SubTask subTask = new SubTask(id, subTaskName, subTaskDescription, subTaskStatus);
        subTasks.put(id, subTask);
        return subTask;
    }

    // Метод создания Эпика
    public Epic creatEpic(String epicName, String epicDescription, String epicStatus) { // параметры изменить
        HashMap<Integer, String> epics;
        id++;
        Epic epic = new Epic(id, epicName, epicDescription, epicStatus, epicName);
        epics.put(id, epic);
        return epic;
    }
*/


    // Метод обновления данных
    /*public void updateTask(Task, task) {
        task.put(task.getId(), task);
    }*/


} // скобка закрывает class TaskManager
