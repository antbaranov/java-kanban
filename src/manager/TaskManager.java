package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    // Метод создания простой Задачи task
    void addTask(Task task);

    // Метод создания Подзадачи subTask
    void addSubTask(SubTask subTask);

    // Метод создания Эпика tasks.Epic
    void addEpic(Epic epic);

    // Удаление всех Задач
    void deleteTasks();

    // Удаление всех ПодЗадач
    void deleteSubTasks();

    // Удаление всех Эпиков
    void deleteEpics();

    // Получение списка Эпиков
    List<Epic> getEpics();

    // Получение списка задач
    List<Task> getTasks();

    // Получение списка подзадач
    List<SubTask> getSubTask();

    // Получение Задач по идентификатору
    Task getByIdTask(int id);

    // Получение Подзадач по идентификатору
    SubTask getByIdSubTask(int id);

    // Получение Эпика по идентификатору
    Epic getByIdEpic(int id);

    // Обновление Задач
    void updateTask(Task task);

    // Обновление Подзадач
    void updateSubTask(SubTask subTask);

    // Обновление Эпиков
    void updateEpic(Epic epic);

    // Обновление статуса Эпиков
    void updateEpicStatus(Epic epic);

    // Удаление Задачи по идентификатору
    void deleteByIdTask(int id);

    // Удаление Подзадачи по идентификатору
    void deleteByIdSubTask(int id);

    // Удаление Эпика по идентификатору
    void deleteByIdEpic(int id);

    // Получение списка всех подзадач определённого эпика
    ArrayList<SubTask> getSubTasksOfEpic(int id);

    // История просмотров задач
    HistoryManager getHistoryManager();
}
