package manager;

import exceptions.ManagerValidateException;
import tasks.Epic;
import constants.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private static int getIdCounter = 1; // Объявление , инициализация начального идентификатора
    private static final Map<Integer, Task> tasks = new HashMap<>();
    private static final Map<Integer, SubTask> subTasks = new HashMap<>();
    private static final Map<Integer, Epic> epics = new HashMap<>();
    private static HistoryManager historyManager = Managers.getDefaultHistory();

    private final Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime);

    protected Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);

    public InMemoryTaskManager(HistoryManager historyManager) {

        this.historyManager = historyManager;
    }


    // Метод создания простой Задачи task
    @Override
    public Task addTask(Task task) {
        if (task == null) return null;
        task.setId(getIdCounter++);
        addNewPrioritizedTask(task);
        tasks.put(task.getId(), task);
        return task;
    }

    // Метод создания Подзадачи subTask
    @Override
    public SubTask addSubTask(SubTask subTask) {
        if (subTask == null) return null;
        subTask.setId(getIdCounter++);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            addNewPrioritizedTask(subTask);
            subTasks.put(subTask.getId(), subTask);
            epic.setSubtaskIds(subTask.getId());
            updateStatusEpic(epic);
            updateTimeEpic(epic);
            return subTask;
        } else {
            System.out.println("Epic not found");
            return null;
        }
    }

    // Метод создания Эпика tasks.Epic
    @Override
    public Epic addEpic(Epic epic) {
        if (epic == null) return null;
        epic.setId(getIdCounter++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    // Удаление всех Задач
    @Override
    public void deleteAllTasks() {
        tasks.clear();
        prioritizedTasks.clear();
    }

    // Удаление всех ПодЗадач
    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            for (int subtaskId : epic.getSubtaskIds()) {
                SubTask subtask = subTasks.get(subtaskId);
                prioritizedTasks.remove(subtask);
                subTasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epic.getSubtaskIds().clear();
        }
    }

    // Удаление всех Эпиков
    @Override
    public void deleteAllEpics() {
        subTasks.clear();
        epics.clear();
    }

    // Удалить все подзадачи по эпику
    @Override
    public void deleteAllSubtasksByEpic(Epic epic) {
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                SubTask subtask = subTasks.get(subtaskId);
                prioritizedTasks.remove(subtask);
                subTasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epic.getSubtaskIds().clear();
        }
    }

    // Получение списка Эпиков
    @Override
    public List<Epic> getAllEpics() {
        if (epics.size() == 0) {
            System.out.println("Epic list is empty");
            return Collections.emptyList();
        }
        return new ArrayList<>(epics.values());
    }

    // Получение списка задач
    @Override
    public List<Task> getAllTasks() {
        if (tasks.size() == 0) {
            System.out.println("Task list is empty");
            return Collections.emptyList();
        }
        return new ArrayList<>(tasks.values());
    }

    // Получение списка подзадач
    @Override
    public List<SubTask> getAllSubtasks() {
        if (subTasks.size() == 0) {
            System.out.println("SubTasks list is empty");
            return Collections.emptyList();
        }
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
        if (task != null && tasks.containsKey(task.getId())) {
            addNewPrioritizedTask(task);
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Task not found");
        }
    }

    // Обновление Подзадач (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTask != null && subTasks.containsKey(subTask.getId())) {
            Epic epic = epics.get(subTask.getEpicId());
            if (epics.containsKey(epic.getId())) {
                addNewPrioritizedTask(subTask);
                subTasks.put(subTask.getId(), subTask);
                updateStatusEpic(epic);
                updateTimeEpic(epic);
            } else {
                System.out.println("SubTask not found");
            }
        }
    }

    // Обновление Эпиков (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateStatusEpic(epic);
            updateTimeEpic(epic);
        } else {
            System.out.println("Epic not found");
        }
    }

    // Обновление статуса Эпиков
    @Override
    public void updateStatusEpic(Epic epic) { // Доработать метод
        List<Integer> subs = epic.getSubtaskIds();
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

    public void updateTimeEpic(Epic epic) {
        List<SubTask> subTasks = getAllSubtasksByEpicId(epic.getId());
        Instant startTime = subTasks.get(0).getStartTime();
        Instant endTime = subTasks.get(0).getEndTime();

        for (SubTask subtask : subTasks) {
            if (subtask.getStartTime().isBefore(startTime)) startTime = subtask.getStartTime();
            if (subtask.getEndTime().isAfter(endTime)) endTime = subtask.getEndTime();
        }

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        long duration = (endTime.toEpochMilli() - startTime.toEpochMilli());
        epic.setDuration(duration);
    }

    // Удаление Задачи по идентификатору
    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            prioritizedTasks.removeIf(task -> task.getId() == id);
            tasks.remove(id);
            historyManager.remove(id);
        } // Проверка на если не найдено?
    }

    // Удаление Подзадачи по идентификатору
    @Override
    public void deleteSubtaskById(int id) {
        int epicId = subTasks.get(id).getEpicId();
        SubTask subTask = subTasks.get(id);
        Epic epic = this.getEpicById(subTask.getEpicId());
        if (subTasks.containsKey(id)) {
            if (epics.containsKey(epic.getId())) {
                epics.get(epicId).getSubtaskIds().remove((Integer) id);
                updateStatusEpic(epic);
                updateTimeEpic(epic);
                prioritizedTasks.remove(subTask);
                subTasks.remove(id);
                historyManager.remove(id);
            }
        }
    }


    // Удаление Эпика по идентификатору
    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            epic.getSubtaskIds().forEach(subTaskId -> {
                prioritizedTasks.removeIf(task -> Objects.equals(task.getId(), subTaskId));
                subTasks.remove(subTaskId);
                historyManager.remove(subTaskId);
            });
            epics.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Epic not found");
        }
    }

    // Получение списка всех подзадач определённого эпика
    @Override
    public List<SubTask> getAllSubtasksByEpicId(int id) {
        if (epics.containsKey(id)) {
            List<SubTask> subtasksNew = new ArrayList<>();
            Epic epic = epics.get(id);
            for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                subtasksNew.add(subTasks.get(epic.getSubtaskIds().get(i)));
            }
            return subtasksNew;
        } else {
            return Collections.emptyList();
        }
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

    public static void addToHistory(int id) {
        if (epics.containsKey(id)) {
            historyManager.addHistory(epics.get(id));
        } else if (subTasks.containsKey(id)) {
            historyManager.addHistory(subTasks.get(id));
        } else if (tasks.containsKey(id)) {
            historyManager.addHistory(tasks.get(id));
        }
    }


    public HistoryManager getHistoryManager() {
        return historyManager;
    }
    @Override
    public void remove(int id) {
        historyManager.remove(id);
    }

    private void addNewPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
        validateTaskPriority();
    }

    public boolean checkTime(Task task) {
        List<Task> tasks = List.copyOf(prioritizedTasks);
        int sizeTimeNull = 0;
        if (tasks.size() > 0) {
            for (Task taskSave : tasks) {
                if (taskSave.getStartTime() != null && taskSave.getEndTime() != null) {
                    if (task.getStartTime().isBefore(taskSave.getStartTime())
                            && task.getEndTime().isBefore(taskSave.getStartTime())) {
                        return true;
                    } else if (task.getStartTime().isAfter(taskSave.getEndTime())
                            && task.getEndTime().isAfter(taskSave.getEndTime())) {
                        return true;
                    }
                } else {
                    sizeTimeNull++;
                }

            }
            return sizeTimeNull == tasks.size();
        } else {
            return true;
        }
    }

    private void validateTaskPriority() {
        List<Task> tasks = getPrioritizedTasks();

        for (int i = 1; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            boolean taskHasIntersections = checkTime(task);

            if (taskHasIntersections) {
                throw new ManagerValidateException(
                        "ВНИМАНИЕ! Задача №" + task.getId() + " и №" + tasks.get(i - 1) + "пересекаются");
            }
        }
    }

    private List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }


    @Override
    public void printTasks() {
        if (tasks.size() == 0) {
            System.out.println("Task list is empty");
            return;
        }
        for (Task task : tasks.values()) {
            System.out.println("Task{" +
                    "description='" + task.getDescription() + '\'' +
                    ", id=" + task.getId() +
                    ", name='" + task.getName() + '\'' +
                    ", status=" + task.getStatus() +
                    '}');
        }
    }

    @Override
    public void printEpics() {
        if (epics.size() == 0) {
            System.out.println("Epic list is empty");
            return;
        }
        for (Epic epic : epics.values()) {
            System.out.println("Epic{" +
                    "subtasksIds=" + epic.getSubtaskIds() +
                    ", description='" + epic.getDescription() + '\'' +
                    ", id=" + epic.getId() +
                    ", name='" + epic.getName() + '\'' +
                    ", status=" + epic.getStatus() +
                    '}');
        }
    }

    @Override
    public void printSubtasks() {
        if (subTasks.size() == 0) {
            System.out.println("Subtask list is empty");
            return;
        }
        for (SubTask subtask : subTasks.values()) {
            System.out.println("Subtask{" +
                    "epicId=" + subtask.getEpicId() +
                    ", description='" + subtask.getDescription() + '\'' +
                    ", id=" + subtask.getId() +
                    ", name='" + subtask.getName() + '\'' +
                    ", status=" + subtask.getStatus() +
                    '}');
        }
    }

}

