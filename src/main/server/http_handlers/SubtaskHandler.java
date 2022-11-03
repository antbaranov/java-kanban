package main.server.http_handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.constants.StatusCode;
import main.manager.TaskManager;
import main.server.InstantAdapter;
import main.tasks.SubTask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class SubtaskHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        int statusCode;
        String response;

        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                String query = exchange.getRequestURI().getQuery();
                if (query == null) {
                    statusCode = StatusCode.CODE_200.getCode();
                    response = gson.toJson(taskManager.getAllSubtasks());
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        SubTask subtask = taskManager.getSubTaskById(id);
                        if (subtask != null) {
                            response = gson.toJson(subtask);
                        } else {
                            response = "ПодЗадача с данным id не найдена";
                        }
                        statusCode = StatusCode.CODE_200.getCode();
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = StatusCode.CODE_400.getCode();
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = StatusCode.CODE_400.getCode();
                        response = "Неверный формат id";
                    }
                }
                break;
            case "POST":
                String bodyRequest = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    SubTask subtask = gson.fromJson(bodyRequest, SubTask.class);
                    int id = subtask.getId();
                    if (taskManager.getSubTaskById(id) != null) {
                        taskManager.updateTask(subtask);
                        statusCode = StatusCode.CODE_200.getCode();
                        response = "ПодЗадача с id=" + id + " обновлена";
                    } else {
                        System.out.println("Создана");
                        SubTask subtaskCreated = taskManager.addSubTask(subtask);
                        System.out.println("Создана ПодЗадача: " + subtaskCreated);
                        int idCreated = subtaskCreated.getId();
                        statusCode = StatusCode.CODE_201.getCode();
                        response = ("Создана подЗадача с id=" + idCreated);
                        System.out.println("Создана подЗадача с id=" + idCreated);
                    }
                } catch (JsonSyntaxException e) {
                    response = "Неверный формат запроса";
                    statusCode = StatusCode.CODE_400.getCode();
                }
                break;
            case "DELETE":
                response = "";
                query = exchange.getRequestURI().getQuery();
                if (query == null) {
                    taskManager.deleteAllSubtasks();
                    statusCode = StatusCode.CODE_200.getCode();
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        taskManager.deleteSubtaskById(id);
                        statusCode = StatusCode.CODE_200.getCode();
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = StatusCode.CODE_400.getCode();
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = StatusCode.CODE_400.getCode();
                        response = "Неверный формат id";
                    }
                }
                break;
            default:
                statusCode = StatusCode.CODE_400.getCode();
                response = "Некорректный запрос";
        }

        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
        exchange.sendResponseHeaders(statusCode, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
