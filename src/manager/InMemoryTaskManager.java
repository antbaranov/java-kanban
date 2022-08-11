package manager;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private int nextId = 1; // Объявление , инициализация начального идентификатора
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    // Метод создания простой Задачи task
    @Override
    public int addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    // Метод создания Подзадачи subTask
    @Override
    public int addSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        subTask.setId(nextId++);
        subTasks.put(subTask.getId(), subTask);
        epic.addSubTask(subTask.getId());
        updateEpicStatus(epic);
        return epic.getId();
    }

    // Метод создания Эпика tasks.Epic
    @Override
    public int addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    // Удаление всех Задач
    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    // Удаление всех ПодЗадач
    @Override
    public void deleteSubTasks() {
        subTasks.clear();
    }

    // Удаление всех Эпиков
    @Override
    public void deleteEpics() {
        subTasks.clear();
        epics.clear();
    }

    // Получение списка Эпиков
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // Получение списка задач
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Получение списка подзадач
    @Override
    public List<SubTask> getSubTask() {
        return new ArrayList<>(subTasks.values());

    }

    // Получение Задач по идентификатору
    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    // Получение Подзадач по идентификатору
    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    // Получение Эпика по идентификатору
    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    // Обновление Задач (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    // Обновление Подзадач (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    @Override
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
    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    // Обновление статуса Эпиков
    @Override
    public void updateEpicStatus(Epic epic) {
        List<Integer> subs = epic.getSubTaskIds();
        if (subs.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        Status status = null;
        for (int id : subs) {
            SubTask subTask = subTasks.get(id);
            if (status == null) {
                status = subTask.getStatus();
                continue;
            }
            if (status.equals(subTask.getStatus())
                    && !status.equals(Status.IN_PROGRESS)) {
                continue;
            }
            epic.setStatus(Status.IN_PROGRESS);
            return;
        }
        epic.setStatus(status);
    }

    // Удаление Задачи по идентификатору
    @Override
    public void deleteByIdTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    // Удаление Подзадачи по идентификатору
    @Override
    public void deleteByIdSubTask(int id) {
        int epicId = subTasks.get(id).getEpicId();
        SubTask subTask = subTasks.get(id);
        Epic epic = this.getEpicById(subTask.getEpicId()); // Так имелось ввиду?
        if (subTasks.containsKey(id)) {
            if (epics.containsKey(epic.getId())) {
                epics.get(epicId).getSubTaskIds().remove((Integer) id);
                subTasks.remove((Integer) id);
                updateEpicStatus(epic);
            }
        }
    }

    // Удаление Эпика по идентификатору
    @Override
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
    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(int id) {
        if (epics.containsKey(id)) {
            ArrayList<SubTask> subTasksNew = new ArrayList<>();
            Epic epic = epics.get(id);
            for (int i = 0; i < epic.getSubTaskIds().size(); i++) {
                subTasksNew.add(subTasks.get(epic.getSubTaskIds().get(i)));
            }
            return subTasksNew;
        }
        return new ArrayList<>();
    }

    // История просмотров задач
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "historyManager=" + historyManager +
                '}';
    }

}


