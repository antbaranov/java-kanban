package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    // Метод создания простой Задачи task
    Task addTask(Task task);

    // Метод создания Подзадачи subTask
    SubTask addSubTask(SubTask subTask);

    // Метод создания Эпика tasks.Epic
    Epic addEpic(Epic epic);

    // Удаление всех Задач
    void deleteAllTasks();

    // Удаление всех ПодЗадач
    void deleteAllSubtasks();

    // Удаление всех Эпиков
    void deleteAllEpics();

    // Удалить все подзадачи по эпику
    void deleteAllSubtasksByEpic(Epic epic);

    // Получение списка Эпиков
    List<Epic> getAllEpics();

    // Получение списка задач
    List<Task> getAllTasks();

    // Получение списка подзадач
    List<SubTask> getAllSubtasks();

    // Получение Задач по идентификатору
    Task getTaskById(int id);

    // Получение Подзадач по идентификатору
    SubTask getSubTaskById(int id);

    // Получение Эпика по идентификатору
    Epic getEpicById(int id);

    // Обновление Задач
    void updateTask(Task task);

    // Обновление Подзадач
    void updateSubTask(SubTask subTask);

    // Обновление Эпиков
    void updateEpic(Epic epic);

    // Обновление статуса Эпиков
    void updateStatusEpic(Epic epic);

    // Удаление Задачи по идентификатору
    void deleteTaskById(int id);

    // Удаление Подзадачи по идентификатору
    void deleteSubtaskById(int id);

    // Удаление Эпика по идентификатору
    void deleteEpicById(int id);
    void removeEpicById(int id);
    void deleteByIdEpic(int id);

    // Получение списка всех подзадач определённого эпика
   List<SubTask> getAllSubtasksByEpicId(int id);
    void remove(int id);

    void printTasks();

    void printEpics();

    void printSubtasks();


    // История просмотров задач
    List<Task> getHistory();


}
