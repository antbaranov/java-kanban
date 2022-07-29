import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int nextId = 1; // Объявление, инициализация начального идентификатора
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();

    // Метод создания простой Задачи task
    public int addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    // Метод создания Подзадачи subTask
    public int addSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        subTask.setId(nextId++);
        subTasks.put(subTask.getId(), subTask);
        epic.addSubTask(subTask.getId());
        updateEpicStatus(epic);
        return subTask.getId();
    }

    // Метод создания Эпика Epic
    public int addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    // Удаление всех Задач
    public void deleteTasks() {
        tasks.clear();
    }

    // Удаление всех ПодЗадач
    public void deleteSubTasks() {
        subTasks.clear();
    }

    // Удаление всех Эпиков
    public void deleteEpics() {
        subTasks.clear();
        epics.clear();
    }

    // Получение Задач по идентификатору
    public Task getByIdTask(int id) {
        return tasks.get(id);
    }

    // Получение Подзадач по идентификатору
    public SubTask getByIdSubTask(int id) {
        return subTasks.get(id);
    }

    // Получение Эпика по идентификатору
    public Epic getByIdEpic(int id) {
        return epics.get(id);
    }

    // Обновление Задач (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) { // Филипп говорил, что проверки делать не надо
            tasks.put(task.getId(), task);
        }
    }

    // Обновление Подзадач (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            Epic epic = epics.get(subTask.getEpicId());
            if (epics.containsKey(epic.getId())) {
                subTasks.put(subTask.getId(), subTask);
                updateEpicStatus(epic);
            }
        }
    }

    // Обновление Эпиков (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    // _________________________Обновление статуса Эпиков_____________________________________________________________

 /* Алгоритм можно реализовать чуть проще:
    Обозначаем переменную status = null
    Проходимся по всем подзадачам эпика и смотрим
 - если статус null, то проставляем статус подзадачи и переходим к следующему шагу цикла (continue;)
            - если статус эпика равен статусу подзадачи и (&&) статус эпика не равен IN_PROGRESS тоже переходим к следующем шагу цикла
 - проставляем эпику статус IN_PROGRESS
    выходим из цикла (return;)
    После цикла проставляем эпику статус переменной status */

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subs = epic.getSubTaskIds(); //  Получаем список подзадач:
        if (subs.isEmpty()) { // Проверяем
            epic.setStatus("NEW");
            return;
        }
//        А дальше запускаем алгоритм
        String status = null;
        for (int i = 0; i < epic.getSubTaskIds().size(); i++) { // Можно сделать с помощью foreach for (int id : subs)
            SubTask subTask = subTasks.get(i);
            if (subTask.getStatus() == null) {
                status = "NEW";

                if (epic.getStatus() == null) { // если статус null, то проставляем статус подзадачи
                    status = subTask.getStatus();
                    continue;
                }

            /* Если статус равен статусу подзадачи и (&&) статус не равен IN_PROGRESS
            тоже переходим к следующем шагу цикла  status == subTasks.getStatus() и status != "IN_PROGRESS"
            Прости, пожалуйста, вот здесь, наверное, запутала,
            так как мы работаем с переменной status надо сравнивать ее,
            а не статус, записанный в эпике
            Кстати, можно оставить просто if */
            }
            if ((epic.getStatus() == subTask.getStatus()) && (epic.getStatus() != "IN_PROGRESS")) {
                // тоже переходим к следующем шагу цикла
                //  Пока статус не проставляем
                continue;
            } if ((status == subTask.getStatus()) && (status != "IN_PROGRESS")) {
            /* А вот здесь нужно поставить status = "IN_PROGRESS";
                    - проставляем эпику статус IN_PROGRESS
                    - выходим из цикла (return;)
                    а потом делаем return; */
                status = "IN_PROGRESS";
                continue;
            }
            return;
        }
        epic.setStatus(status); // После цикла проставляем эпику статус переменной status
    }

    // Удаление Задачи по идентификатору
    public void deleteByIdTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    // Удаление Подзадачи по идентификатору
    public void deleteByIdSubTask(int id) {
        int epicId = subTasks.get(id).getEpicId();
        SubTask subTask = subTasks.get(id);
        Epic epic = getByIdEpic(subTask.getEpicId()); // Так имелось ввиду?
        if (subTasks.containsKey(id)) {
            if (epics.containsKey(epic.getId())) {
                epics.get(epicId).getSubTaskIds().remove((Integer) id);
                subTasks.remove((Integer) id);
                updateEpicStatus(epic);
            }
        }
    }

    // Удаление Эпика по идентификатору
    public void deleteByIdEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (int subTaskId : epic.getSubTaskIds()) {
                subTasks.remove(subTaskId);
            }
            epics.remove(id);
        }
    }

    // Получение списка всех подзадач определённого эпика
    public ArrayList<SubTask> getSubTasksOfEpic(int id) {
        if (epics.containsKey(id)) {
            ArrayList<SubTask> subTasksNew = new ArrayList<>();
            Epic epic = epics.get(id);
            for (int i = 0; i < epic.getSubTaskIds().size(); i++) {
                subTasksNew.add(subTasks.get(epic.getSubTaskIds().get(i)));
            }
            return subTasksNew;
        }
        return new ArrayList<>(); /* Лучше в таком случае возвращать пустой список new ArrayList<>();
                                    Когда мы явно возвращаем в методе null,
                                    то нужно не забывать его обрабатывать:
                                    при использовании проверять, что получили не null,
                                    потому что если мы об этом забудем,
                                    программа выкинет NullPointerException */
    }
}
