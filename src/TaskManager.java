/*import tasks.Epic;
import tasks.Subtask;
import tasks.Task;*/
import java.util.List;

public interface TaskManager {

    void addTask(Task task); // Метод создания простой Задачи task

    void addSubTask(SubTask subTask); // Метод создания Подзадачи subTask

    void addEpic(Epic epic); // Метод создания Эпика Epic

    void updateTask(Task task); // Обновление Задач

    void updateEpic(Epic epic); // Обновление Эпиков

    void updateSubTask(SubTask subTask); // Обновление Подзадач

    List<Epic> getEpics();

    List<Task> getTasks();

    List<SubTask> getSubTask();

    void deleteTasks(); // Удаление всех Задач

    void deleteEpics(); // Удаление всех Эпиков

    void deleteSubTasks(); // Удаление всех ПодЗадач

    Task getByIdTask(int id); // Получение Задач по идентификатору

    Epic getByIdEpic(int id); // Получение Эпика по идентификатору

    SubTask getByIdSubTask(int id); // Получение Подзадач по идентификатору

    void deleteByIdTask(int id); // Удаление Задачи по идентификатору

    void deleteByIdEpic(int id); // Удаление Эпика по идентификатору

    void deleteByIdSubTask(int id); // Удаление Подзадачи по идентификатору

    void updateEpicStatus(Epic epic); // Обновление статуса Эпиков

    List<Task> getHistory(); // История просмотров задач
}
