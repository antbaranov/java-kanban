package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    // Метод создания простой Задачи task
    public void addTask(Task task);

    // Метод создания Подзадачи subTask
    public void addSubTask(SubTask subTask);

    // Метод создания Эпика tasks.Epic
    public void addEpic(Epic epic);

    // Удаление всех Задач
    public void deleteTasks();

    // Удаление всех ПодЗадач
    public void deleteSubTasks();

    // Удаление всех Эпиков
    public void deleteEpics();

    // Получение списка Эпиков
    public List<Epic> getEpics();

    // Получение списка задач
    public List<Task> getTasks();

    // Получение списка подзадач
    public List<SubTask> getSubTask();

    // Получение Задач по идентификатору
    public Task getByIdTask(int id);

    // Получение Подзадач по идентификатору
    public SubTask getByIdSubTask(int id);

    // Получение Эпика по идентификатору
    public Epic getByIdEpic(int id);

    // Обновление Задач
    public void updateTask(Task task);

    // Обновление Подзадач
    public void updateSubTask(SubTask subTask);

    // Обновление Эпиков
    public void updateEpic(Epic epic);

    // Обновление статуса Эпиков
    public void updateEpicStatus(Epic epic);

    // Удаление Задачи по идентификатору
    public void deleteByIdTask(int id);

    // Удаление Подзадачи по идентификатору
    public void deleteByIdSubTask(int id);

    // Удаление Эпика по идентификатору
    public void deleteByIdEpic(int id);

    // Получение списка всех подзадач определённого эпика
    public ArrayList<SubTask> getSubTasksOfEpic(int id);

    // История просмотров задач
    public List<Task> getHistory();
}
