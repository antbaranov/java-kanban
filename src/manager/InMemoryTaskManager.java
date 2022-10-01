package manager;

import constants.TaskStatus;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private static int idCounter = 1; // Объявление , инициализация начального идентификатора
    private static final Map<Integer, Task> tasks = new HashMap<>();
    private static final Map<Integer, SubTask> subTasks = new HashMap<>();
    private static final Map<Integer, Epic> epics = new HashMap<>();
    private static final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {

    }

    protected Set<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime() == null && o2.getStartTime() == null) return o1.getId() - o2.getId();
        if (o1.getStartTime() == null) return 1;
        if (o2.getStartTime() == null) return -1;
        if (o1.getStartTime().isAfter(o2.getStartTime())) return 1;
        if (o1.getStartTime().isBefore(o2.getStartTime())) return -1;
        if (o1.getStartTime().isEqual(o2.getStartTime())) return o1.getId() - o2.getId();
        return 0;
    });

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        InMemoryTaskManager.idCounter = idCounter;
    }

    // Метод создания простой Задачи task
    @Override
    public int addTask(Task task) {
        task.setId(idCounter++);
        getTaskEndTime(task);
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    // Метод создания Подзадачи subTask
    @Override
    public int addSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        getSubtaskEndTime(subTask);
        prioritizedTasks.add(subTask);
        Epic epic = epics.get(epicId);
        subTask.setId(idCounter++);
        subTasks.put(subTask.getId(), subTask);
        epic.addSubTask(subTask.getId());
        updateEpicStatus(epic);
        getEpicTimesAndDuration(epics.get(subTask.getEpicId())); // ?????
        return subTask.getId();
    }

    // Метод создания Эпика tasks.Epic
    @Override
    public int addEpic(Epic epic) {
        epic.setId(idCounter++);
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
        historyManager.addHistory(tasks.get(id));
        return tasks.get(id);
    }

    // Получение Подзадач по идентификатору
    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.addHistory(subTasks.get(id));
        return subTasks.get(id);
    }

    // Получение Эпика по идентификатору
    @Override
    public Epic getEpicById(int id) {
        historyManager.addHistory(epics.get(id));
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
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        TaskStatus status = null;
        for (int id : subs) {
            SubTask subTask = subTasks.get(id);
            if (status == null) {
                status = subTask.getStatus();
                continue;
            }
            if (status.equals(subTask.getStatus())
                    && !status.equals(TaskStatus.IN_PROGRESS)) {
                continue;
            }
            epic.setStatus(TaskStatus.IN_PROGRESS);
            return;
        }
        epic.setStatus(status);
    }

    // Удаление Задачи по идентификатору
    @Override
    public void deleteByIdTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
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
                updateEpicStatus(epic);
                subTasks.remove((Integer) id);
                historyManager.remove(id);
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
                historyManager.remove(subTaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllEpicsAndSubTasks() {
        epics.clear();
        subTasks.clear();
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

    public HistoryManager getHistoryManager() {
        return historyManager;
    }


    public static void addToHistory(int id) {
        if (epics.containsKey(id)) {
            historyManager.addHistory(epics.get(id));
        } else if (subTasks.containsKey(id)) {
            historyManager.addHistory(subTasks.get(id));
        } else if (tasks.containsKey(id)) {
            historyManager.addHistory(tasks.get(id));
        }
    }

    @Override
    public void intersectionCheck() {
        LocalDateTime checkTime = null;
        boolean flagCheckTimeIsEmpty = true;
        for (Task task : prioritizedTasks) {
            if (flagCheckTimeIsEmpty) {
                checkTime = task.getEndTime();
                flagCheckTimeIsEmpty = false;
            } else if (task.getStartTime() != null) {
                if (task.getStartTime().isBefore(checkTime)) {
                    throw new ManagerSaveException("Найдено пересечение времени задач, проверьте корректность данных");
                }
                if (task.getStartTime().isAfter(checkTime) || task.getStartTime().isEqual(checkTime)) {
                    checkTime = task.getEndTime();
                }
            }
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        intersectionCheck();
        return prioritizedTasks;
    }

    @Override
    public void getTaskEndTime(Task task) {
        if (task.getStartTime() == null || task.getDuration() == null) return;
        LocalDateTime endTime = task.getStartTime().plus(task.getDuration());
        task.setEndTime(endTime);
    }

    @Override
    public void getEpicTimesAndDuration(Epic epic) {
        if (epic.getSubTaskIds().isEmpty()) {
            return;
        }
        LocalDateTime start;
        LocalDateTime end;
        start = subTasks.get(epic.getSubTaskIds().get(0)).getStartTime();
        end = subTasks.get(epic.getSubTaskIds().get(0)).getEndTime();
        epic.setStartTime(start);
        epic.setEndTime(end);
        for (Integer id : epic.getSubTaskIds()) {
            if (subTasks.get(id).getStartTime() != null && subTasks.get(id).getStartTime().isBefore(start)) {
                start = subTasks.get(id).getStartTime();
            }
            if (subTasks.get(id).getStartTime() != null && subTasks.get(id).getEndTime().isAfter(end)) {
                end = subTasks.get(id).getEndTime();
            }
        }
        epic.setStartTime(start);
        epic.setEndTime(end);
        epic.setDuration(Duration.between(epic.getStartTime(), epic.getEndTime()));
    }

    @Override
    public void getSubtaskEndTime(SubTask subtask) {
        if (subtask.getStartTime() == null || subtask.getDuration() == null) return;
        LocalDateTime endTime = subtask.getStartTime().plus(subtask.getDuration());
        subtask.setEndTime(endTime);
        if (epics.containsKey(subtask.getEpicId())) {
            getEpicTimesAndDuration(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "nextId=" + idCounter +
                ", prioritizedTasks=" + prioritizedTasks +
                '}';
    }

}