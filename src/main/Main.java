import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import constants.Status;
import server.KVServer;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import server.InstantAdapter;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.time.Instant;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer server;
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Instant.class, new InstantAdapter())
                    .create();

            server = new KVServer();
            server.start();
            HistoryManager historyManager = Managers.getDefaultHistory();
            TaskManager httpTaskManager = Managers.getDefault(historyManager);

            Task task1 = new Task("Title Task 1", "Description Task 1", Status.NEW, Instant.now(), 15);
            httpTaskManager.addTask(task1);
            Task task2 = new Task("Title Task 2", "Description Task 2", Status.NEW, Instant.now(), 15);
            httpTaskManager.addTask(task2);

            Epic epic1 = new Epic("Title Epic 1", "Description Epic 1", Status.NEW, Instant.now(), 45);
            httpTaskManager.addEpic(epic1);

            SubTask subTask1 = new SubTask(
                    "Title SubTask 1", "Description SubTask 1", Status.NEW, epic1.getId(), Instant.now(), 15);
            httpTaskManager.addSubTask(subTask1);
            SubTask subTask2 = new SubTask(
                    "Title SubTask 2", "Description SubTask 2", Status.NEW, epic1.getId(), Instant.now(), 15);
            httpTaskManager.addSubTask(subTask2);
            SubTask subTask3 = new SubTask(
                    "Title SubTask 3", "Description SubTask 3", Status.NEW, epic1.getId(), Instant.now(), 15);
            httpTaskManager.addSubTask(subTask3);


            httpTaskManager.getTaskById(task1.getId());
            httpTaskManager.getEpicById(epic1.getId());
            httpTaskManager.getSubTaskById(subTask1.getId());
            httpTaskManager.getSubTaskById(subTask2.getId());
            httpTaskManager.getSubTaskById(subTask3.getId());

            System.out.println("\nВывод всех задач");
            System.out.println(gson.toJson(httpTaskManager.getAllTasks()));
            System.out.println("\nВывод всех эпиков");
            System.out.println(gson.toJson(httpTaskManager.getAllEpics()));
            System.out.println("\nВывод всех подзадач");
            System.out.println(gson.toJson(httpTaskManager.getAllSubtasks()));
            System.out.println("\nЗагруженный менеджер");
            System.out.println(httpTaskManager);
            server.stop();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}