import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int nextId = 1; // Объявление, инициализация начального идентификатора

    // Метод создания простой Задачи task
    private HashMap<Integer, Task> tasks = new HashMap<>();

    public int creatTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    // Метод создания Подзадачи subTask

    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void creatSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subTask.setId(nextId++);
        subTasks.put(subTask.getId(), subTask);
        epic.addSubTask(subTask.getId());
        updateEpicStatus(epic);

        System.out.println("epic.getSubTaskIds() : " + epic.getSubTaskIds());

      //  return subTask.getId();
    }

    // Метод создания Эпика Epic
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public int creatEpic(Epic epic) {
        epic.setId(nextId++);

        epics.put(epic.getId(), epic);
        return epic.getId();
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
    }

    // Обновление Подзадач (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
//        updateEpicStatus(epic); // создать метод обнговления статуса
    }

    // Обновление Эпиков (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);

    }

    // Обновление статуса Эпиков
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

    // Получение списка всех подзадач определённого эпика - не понимаю как делать
    public String getSubTasksOfEpic(int epicId) {
        return "Список подзадач Эпика: " + epics.get(epicId);
     }


} // !!! Cкобка закрывает class Manager