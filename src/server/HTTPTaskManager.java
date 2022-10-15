package server;

import com.google.gson.*;
import manager.FileBackedTasksManager;
import manager.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {

    private final KVTaskClient client;

    private static final Gson gson =
            new GsonBuilder()
                    .registerTypeAdapter(Instant.class, new InstantAdapter())
                    .create();

    public HTTPTaskManager(HistoryManager historyManager, String path) throws IOException, InterruptedException {
        super(historyManager);
        client = new KVTaskClient(path);

        JsonElement jsonTasks = JsonParser.parseString(client.load("tasks"));
        if (!jsonTasks.isJsonNull()) {
            JsonArray jsonTasksArray = jsonTasks.getAsJsonArray();
            for (JsonElement jsonTask : jsonTasksArray) {
                Task task = gson.fromJson(jsonTask, Task.class);
                this.addTask(task);
            }
        }

        JsonElement jsonEpics = JsonParser.parseString(client.load("epics"));
        if (!jsonEpics.isJsonNull()) {
            JsonArray jsonEpicsArray = jsonEpics.getAsJsonArray();
            for (JsonElement jsonEpic : jsonEpicsArray) {
                Epic task = gson.fromJson(jsonEpic, Epic.class);
                this.addEpic(task);
            }
        }

        JsonElement jsonSubtasks = JsonParser.parseString(client.load("subtasks"));
        if (!jsonSubtasks.isJsonNull()) {
            JsonArray jsonSubtasksArray = jsonSubtasks.getAsJsonArray();
            for (JsonElement jsonSubtask : jsonSubtasksArray) {
                SubTask task = gson.fromJson(jsonSubtask, SubTask.class);
                this.addSubTask(task);
            }
        }

        JsonElement jsonHistoryList = JsonParser.parseString(client.load("history"));
        if (!jsonHistoryList.isJsonNull()) {
            JsonArray jsonHistoryArray = jsonHistoryList.getAsJsonArray();
            for (JsonElement jsonTaskId : jsonHistoryArray) {
                int taskId = jsonTaskId.getAsInt();
                if (this.subTasks.containsKey(taskId)) {
                    this.getSubTaskById(taskId);
                } else if (this.epics.containsKey(taskId)) {
                    this.getEpicById(taskId);
                } else if (this.tasks.containsKey(taskId)) {
                    this.getTaskById(taskId);
                }
            }
        }
    }

    @Override
    public void save() {
        client.put("tasks", gson.toJson(tasks.values()));
        client.put("subtasks", gson.toJson(subTasks.values()));
        client.put("epics", gson.toJson(epics.values()));
        client.put("history", gson.toJson(this.getHistory()
                        .stream()
                        .map(Task::getId)
                        .collect(Collectors.toList())));
    }
}