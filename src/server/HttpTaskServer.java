package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;


public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer httpServer;
    private  Gson gson;
    private TaskManager taskManager;

    public HttpTaskServer() throws IOException, InterruptedException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault(historyManager);
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/", new TasksHandler(taskManager));
        httpServer.createContext("/tasks/task/", new TaskHandler(taskManager));
        httpServer.createContext("/tasks/epic/", new EpicHandler(taskManager));
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler(taskManager));
        httpServer.createContext("/tasks/subtask/epic/", new SubtaskByEpicHandler(taskManager));
        httpServer.createContext("/tasks/history/", new HistoryHandler(taskManager));

    }

    public void start() {
        System.out.println("HTTP-cервер запущен на " + PORT + " порту!");
        System.out.println("HTTP server running on " + PORT + " port!");
        System.out.println("http://localhost:" + PORT + "/tasks/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-cервер остановлен на " + PORT + " порту!");
        System.out.println("HTTP server stopped on " + PORT + " port!");
    }


    public class TasksHandler implements HttpHandler {
        private final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
        private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
        private final TaskManager taskManager;

        public TasksHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            int statusCode = 400;
            String response;
            String method = httpExchange.getRequestMethod();
            String path = String.valueOf(httpExchange.getRequestURI());

            System.out.println("Обрабатывается запрос " + path + " с методом " + method);

            switch (method) {
                case "GET":
                    statusCode = 200;
                    response = gson.toJson(taskManager.getPrioritizedTasks());
                    break;
                default:
                    response = "Некорректный запрос";
            }

            httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
            httpExchange.sendResponseHeaders(statusCode, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    public class TaskHandler implements HttpHandler {
        private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
        private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
        private final TaskManager taskManager;

        public TaskHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            int statusCode;
            String response;
            String method = httpExchange.getRequestMethod();
            String path = String.valueOf(httpExchange.getRequestURI());

            System.out.println("Обрабатывается запрос " + path + " с методом " + method);

            switch (method) {
                case "GET":
                    String query = httpExchange.getRequestURI().getQuery();
                    if (query == null) {
                        statusCode = 200;
                        String jsonString = gson.toJson(taskManager.getAllTasks());
                        System.out.println("GET TASKS: " + jsonString);
                        response = gson.toJson(jsonString);
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                            Task task = taskManager.getTaskById(id);
                            if (task != null) {
                                response = gson.toJson(task);
                            } else {
                                response = "Задача с данным id не найдена";
                            }
                            statusCode = 200;
                        } catch (StringIndexOutOfBoundsException e) {
                            statusCode = 400;
                            response = "В запросе отсутствует необходимый параметр id";
                        } catch (NumberFormatException e) {
                            statusCode = 400;
                            response = "Неверный формат id";
                        }
                    }
                    break;
                case "POST":
                    String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    try {
                        Task task = gson.fromJson(bodyRequest, Task.class);
                        int id = task.getId();
                        if (taskManager.getTaskById(id) != null) {
                            taskManager.updateTask(task);
                            statusCode = 201;
                            response = "Задача с id=" + id + " обновлена";
                        } else {
                            Task taskCreated = taskManager.addTask(task);
                            System.out.println("CREATED TASK: " + taskCreated);
                            int idCreated = taskCreated.getId();
                            statusCode = 201;
                            response = "Создана задача с id=" + idCreated;
                        }
                    } catch (JsonSyntaxException e) {
                        statusCode = 400;
                        response = "Неверный формат запроса";
                    }
                    break;
                case "DELETE":
                    response = "";
                    query = httpExchange.getRequestURI().getQuery();
                    if (query == null) {
                        taskManager.deleteAllTasks();
                        statusCode = 200;
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                            taskManager.deleteTaskById(id);
                            statusCode = 200;
                        } catch (StringIndexOutOfBoundsException e) {
                            statusCode = 400;
                            response = "В запросе отсутствует необходимый параметр id";
                        } catch (NumberFormatException e) {
                            statusCode = 400;
                            response = "Неверный формат id";
                        }
                    }
                    break;
                default:
                    statusCode = 400;
                    response = "Некорректный запрос";
            }

            httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
            httpExchange.sendResponseHeaders(statusCode, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

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
                        statusCode = 200;
                        String jsonString = gson.toJson(taskManager.getAllEpics());
                        System.out.println("GET EPICS: " + jsonString);
                        response = gson.toJson(jsonString);
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                            Epic epic = taskManager.getEpicById(id);
                            if (epic != null) {
                                response = gson.toJson(epic);
                            } else {
                                response = "Эпик с данным id не найден";
                            }
                            statusCode = 200;
                        } catch (StringIndexOutOfBoundsException e) {
                            statusCode = 400;
                            response = "В запросе отсутствует необходимый параметр id";
                        } catch (NumberFormatException e) {
                            statusCode = 400;
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
                            statusCode = 200;
                            response = "Эпик с id=" + id + " обновлен";
                        } else {
                            System.out.println("CREATED");
                            Epic epicCreated = taskManager.addEpic(epic);
                            System.out.println("CREATED EPIC: " + epicCreated);
                            int idCreated = epicCreated.getId();
                            statusCode = 201;
                            response = "Создан эпик с id=" + idCreated;
                        }
                    } catch (JsonSyntaxException e) {
                        statusCode = 400;
                        response = "Неверный формат запроса";
                    }
                    break;
                case "DELETE":
                    response = "";
                    query = exchange.getRequestURI().getQuery();
                    if (query == null) {
                        taskManager.deleteAllEpics();
                        statusCode = 200;
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                            taskManager.deleteEpicById(id);
                            statusCode = 200;
                        } catch (StringIndexOutOfBoundsException e) {
                            statusCode = 400;
                            response = "В запросе отсутствует необходимый параметр id";
                        } catch (NumberFormatException e) {
                            statusCode = 400;
                            response = "Неверный формат id";
                        }
                    }
                    break;
                default:
                    statusCode = 400;
                    response = "Некорректный запрос";
            }

            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
            exchange.sendResponseHeaders(statusCode, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

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
                        statusCode = 200;
                        response = gson.toJson(taskManager.getAllSubtasks());
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                            SubTask subtask = taskManager.getSubTaskById(id);
                            if (subtask != null) {
                                response = gson.toJson(subtask);
                            } else {
                                response = "Подзадача с данным id не найдена";
                            }
                            statusCode = 200;
                        } catch (StringIndexOutOfBoundsException e) {
                            statusCode = 400;
                            response = "В запросе отсутствует необходимый параметр id";
                        } catch (NumberFormatException e) {
                            statusCode = 400;
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
                            statusCode = 200;
                            response = "Подзадача с id=" + id + " обновлена";
                        } else {
                            System.out.println("CREATED");
                            SubTask subtaskCreated = taskManager.addSubTask(subtask);
                            System.out.println("CREATED SUBTASK: " + subtaskCreated);
                            int idCreated = subtaskCreated.getId();
                            statusCode = 201;
                            response = "Создана подзадача с id=" + idCreated;
                        }
                    } catch (JsonSyntaxException e) {
                        response = "Неверный формат запроса";
                        statusCode = 400;
                    }
                    break;
                case "DELETE":
                    response = "";
                    query = exchange.getRequestURI().getQuery();
                    if (query == null) {
                        taskManager.deleteAllSubtasks();
                        statusCode = 200;
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                            taskManager.deleteSubtaskById(id);
                            statusCode = 200;
                        } catch (StringIndexOutOfBoundsException e) {
                            statusCode = 400;
                            response = "В запросе отсутствует необходимый параметр id";
                        } catch (NumberFormatException e) {
                            statusCode = 400;
                            response = "Неверный формат id";
                        }
                    }
                    break;
                default:
                    statusCode = 400;
                    response = "Некорректный запрос";
            }

            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
            exchange.sendResponseHeaders(statusCode, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    public class SubtaskByEpicHandler implements HttpHandler {
        private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
        private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
        private final TaskManager taskManager;

        public SubtaskByEpicHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            int statusCode = 400;
            String response;
            String method = httpExchange.getRequestMethod();
            String path = String.valueOf(httpExchange.getRequestURI());

            System.out.println("Обрабатывается запрос " + path + " с методом " + method);

            switch (method) {
                case "GET":
                    String query = httpExchange.getRequestURI().getQuery();
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        statusCode = 200;
                        response = gson.toJson(taskManager.getSubTaskById(id));
                    } catch (StringIndexOutOfBoundsException | NullPointerException e) {
                        response = "В запросе отсутствует необходимый параметр - id";
                    } catch (NumberFormatException e) {
                        response = "Неверный формат id";
                    }
                    break;
                default:
                    response = "Некорректный запрос";
            }

            httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
            httpExchange.sendResponseHeaders(statusCode, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    public class HistoryHandler implements HttpHandler {
        private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
        private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
        private final TaskManager taskManager;

        public HistoryHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            int statusCode = 400;
            String response;
            String method = httpExchange.getRequestMethod();
            String path = String.valueOf(httpExchange.getRequestURI());

            System.out.println("Обрабатывается запрос " + path + " с методом " + method);

            switch (method) {
                case "GET":
                    statusCode = 200;
                    response = gson.toJson(taskManager.getHistory());
                    break;
                default:
                    response = "Некорректный запрос";
            }

            httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
            httpExchange.sendResponseHeaders(statusCode, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}

