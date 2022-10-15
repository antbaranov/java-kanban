package server.http_handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import constants.StatusCode;
import manager.TaskManager;
import server.InstantAdapter;
import tasks.Epic;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class EpicHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
                    String jsonString = gson.toJson(taskManager.getAllEpics());
                    System.out.println("GET Все Эпики: " + jsonString);
                    //response = gson.toJson(jsonString);
                    response = jsonString;
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        Epic epic = taskManager.getEpicById(id);
                        if (epic != null) {
                            response = gson.toJson(epic);
                        } else {
                            response = "Эпик с данным id не найден";
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
                    Epic epic = gson.fromJson(bodyRequest, Epic.class);
                    int id = epic.getId();
                    if (taskManager.getEpicById(id) != null) {
                        taskManager.updateTask(epic);
                        statusCode = StatusCode.CODE_200.getCode();
                        response = "Эпик с id=" + id + " обновлен";
                    } else {
                        System.out.println("Создан");
                        Epic epicCreated = taskManager.addEpic(epic);
                        System.out.println("Создан EPIC: " + epicCreated);
                        int idCreated = epicCreated.getId();
                        statusCode = StatusCode.CODE_201.getCode();
                        response = ("Создан Эпик с id=" + idCreated);
                        System.out.println("Создан Эпик с id=" + idCreated);

                    }
                } catch (JsonSyntaxException e) {
                    statusCode = StatusCode.CODE_400.getCode();
                    response = "Неверный формат запроса";
                }
                break;
            case "DELETE":
                response = "";
                query = exchange.getRequestURI().getQuery();
                if (query == null) {
                    taskManager.deleteAllEpics();
                    statusCode = StatusCode.CODE_200.getCode();
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        taskManager.deleteEpicById(id);
                        statusCode = StatusCode.CODE_200.getCode();
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = StatusCode.CODE_400.getCode();
                        response = "В запросе отсутствует параметр id";
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
