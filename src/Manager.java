import java.util.HashMap;

public class Manager {
    int id;

    // Метод создания простой Задачи task
    HashMap<Integer, Task> tasks = new HashMap<>();


    public Task creatTask(String name, String description, String status) {
        id++;
        Task task = new Task(id, name, description, status);
        tasks.put(id, task);
        return task;
    }

    // Метод создания Подзадачи subTask
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

    // Получение списка всех Задач
    public String printTasks() {
        return "Список задач: " + tasks;
    }

    // Получение списка всех ПодЗадач
    public String printSubTasks() {
        return "Список Подзадач: " + subTasks;
    }

    // Получение списка всех Эпиков
    public String printEpics() {
        return "Список Эпиков: " + epics;
    }

    // Удаление всех Задач
    public String deleteTasks() {
        tasks.clear();
        return "Все задачи удалены!";
    }

    // Удаление всех ПодЗадач
    public String deleteSubTasks() {
        subTasks.clear();
        return "Все Подзадачи удалены!";
    }

    // Удаление всех Эпиков
    public String deleteEpics() {
        subTasks.clear();
        epics.clear();
        return "Все Эпики с подзадачами удалены!";
    }

    // Получение Задач по идентификатору
    public String getByIdTask(int id) {
        return "Задача по выбранному id: " + tasks.get(id);
    }

    // Получение ПодЗадачи по идентификатору
    public String getByIdSubTask(int id) {
        return "Подзадача по выбранному id: " + subTasks.get(id);
    }

    // Получение Эпика по идентификатору
    public String getByIdEpic(int id) {
        return "Эпик по выбранному id: " + epics.get(id);
    }

 // Обновление Задач (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        System.out.println("Обновление задачи. Список обновленных задач: " + tasks);
    }

    // Удаление Задачи по идентификатору
    public String deleteByIdTask(int id) {
        tasks.remove(id);
        return "Задача по id удалена!";
    }

    // Удаление Подзадачи по идентификатору
    public String deleteByIdSubTask(int id) {
        subTasks.remove(id);
        return "Подзадача по id удалена!";
    }

    // Удаление Эпика по идентификатору
    public String deleteByIdEpic(int id) {
        epics.remove(id);
        return "Эпик по id удален!";
    }


} // скобка закрывает class TaskManager
