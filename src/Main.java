import constants.TaskType;
import server.HTTPTaskManager;
import manager.Managers;
import manager.TaskManager;
import server.KVServer;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        TaskManager manager = Managers.getDefault();

        Task task = new Task(TaskType.TASK,"Title Task 1", "Description Task 1",  Instant.now(),0L);
        manager.addTask(task);

        Epic epic = new Epic(TaskType.TASK,"Title Epic 1", "Description Epic 1",  Instant.now(),0L);
        manager.addEpic(epic);

        SubTask subTask = new SubTask(TaskType.TASK,"Title SubTask 1", "Description Subtask 1",   epic.getId(), Instant.now(), 0L);
        manager.addSubTask(subTask);

        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubTaskById(subTask.getId());

        TaskManager newManager = HTTPTaskManager.loadFromServer(KVServer.PORT);

        final List<Task> tasks = newManager.getAllTasks();
        final List<Epic> epics = newManager.getAllEpics();
        final List<SubTask> subtasks = newManager.getAllSubtasks();
        System.out.println("tasks = " + tasks);
        System.out.println("epics = " + epics);
        System.out.println("subtasks = " + subtasks);
        System.out.println("History = " + manager.getHistory());
    }
}
