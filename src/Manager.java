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
        /*if (epic == null) {
            return;
        }*/
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
        int epicId = subTasks.get(id).getEpicId();
        epics.get(epicId).subTaskIds.remove((Integer) id);
        subTasks.remove((Integer) id);
        SubTask subTask = subTasks.get(id);
        Epic epic = getByIdEpic(subTask.getEpicId());
        updateEpicStatus(epic);
        return "Подзадача по id удалена!";
    }

//    public String deleteByIdSubTask(int id) {
//        SubTask subTask = subTasks.get(id);
//        Epic epic = getByIdEpic(subTask.getEpicId());
//        epic.getSubTaskIds().remove((Integer)subTasks.getId());
////        epic.getSubTaskIds().remove(Integer.valueOf(subTasks.getId()));
//        subTasks.remove(id);
//        updateEpicStatus(epic);
//        return "Подзадача по id удалена!";
//    }

    // Удаление Эпика по идентификатору
    public String deleteByIdEpic(int id) {
        Epic epic = epics.get(id);
        for (int subTaskId : epic.getSubTaskIds()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(id);
        return "Эпик по id удален!";
    }

    // Получение списка всех подзадач определённого эпика


   /* public List<SubTask> getSubTasksOfEpic(int epicId) {
        List<SubTask> result = new ArrayList<>();
        subTasks.
    }

for(
    int subTaskId :subTaskIds.getSubTaskIds())

    {
        subTasks.remove(subTaskId);
    }
    //return "Список : " + epic + subTasks.get(subTaskId);
}*/


} // !!! Cкобка закрывает class Manager
