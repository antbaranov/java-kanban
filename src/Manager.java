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

    // С этим методом беда. Не получилось. Либо я устал, либо жара. А точнее, мозгов не хватает ))

 /* Алгоритм можно реализовать чуть проще:
    Обозначаем переменную status = null
    Проходимся по всем подзадачам эпика и смотрим
 - если статус null, то проставляем статус подзадачи и переходим к следующему шагу цикла (continue;)
            - если статус эпика равен статусу подзадачи и (&&) статус эпика не равен IN_PROGRESS тоже переходим к следующем шагу цикла
 - проставляем эпику статус IN_PROGRESS
    выходим из цикла (return;)
    После цикла проставляем эпику статус переменной status*/

/*
    private void updateEpicStatus(Epic epic) {
        String status = null;

        for (int i = 0; i < epic.getSubTaskIds().size(); i++) {
            SubTask subTask = subTasks.get(i);
            if (subTask.getStatus() == null) {
                status = "NEW";
                continue;
            } else if ((epic.getStatus() == subTasks.getStatus()) && (epic.getStatus() != "IN_PROGRESS")) {
                status = "IN_PROGRESS";
                continue;
            }
            return;
        }
        epic.setStatus(status);
    }
*/

    private void updateEpicStatus(Epic epic) {
        if (subTasks.isEmpty()) {
            epic.setStatus("NEW");
        } else {
            ArrayList<SubTask> subTasksNew = new ArrayList<>();
            int counterDone = 0;
            int counterNew = 0;

            for (int i = 0; i < epic.getSubTaskIds().size(); i++) {
                subTasksNew.add(subTasks.get(epic.getSubTaskIds().get(i)));
            }
            if (!subTasks.isEmpty()) {
                epic.setStatus("NEW");
                return;
            }
            for (SubTask value : subTasksNew) {
                switch (value.getStatus()) {
                    case "NEW":
                        counterNew++;
                        break;
                    case "IN_PROGRESS":
                        epic.setStatus("IN_PROGRESS");
                        return;
                    case "DONE":
                        counterDone++;
                        break;
                }
            }
            if (counterDone == subTasksNew.size()) {
                epic.setStatus("DONE");
            } else if (counterNew == subTasksNew.size()) {
                epic.setStatus("NEW");
            } else {
                epic.setStatus("IN_PROGRESS");
            }
        }
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
        return null;
    }
}
