package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private HttpServer httpServer;
    private Gson gson;
    private TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        this.taskManager = Managers.getDefault(historyManager);
        //this.taskManager = taskManager;
        gson = Managers.getGson();
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks/", this::taskHandler);
    }

    public static void main(String[] args) throws IOException {
        final HttpTaskServer server = new HttpTaskServer();
        server.startServer();
    }

    public void startServer() {
        System.out.println("HTTP-cервер запущен на " + PORT + " порту!");
        System.out.println("HTTP server running on " + PORT + " port!");
        System.out.println("http://localhost:" + PORT + "/tasks/");
        httpServer.start();
    }

    public void stopServer() {
        httpServer.stop(0);
        System.out.println("HTTP-cервер остановлен на " + PORT + " порту!");
        System.out.println("HTTP server stopped on " + PORT + " port!");
    }

    public void taskHandler(HttpExchange h) {
        try {
            String requestMethod = h.getRequestMethod();
            String reguestURI = h.getRequestURI().toString();
            String path = h.getRequestURI().getPath().replaceFirst("/tasks/", "");
            String query = h.getRequestURI().getQuery();

            System.out.println("Поступил запрос: " + requestMethod + ", путь: " + path + ", запрос: " + query);
            System.out.println("Request received: " + requestMethod + ", path: " + path + ", query: " + query);

            switch (requestMethod) {
                case "GET": {
                    if (path.equals("")) {
                        System.out.println("Получаем Приоритеты \nGet priorities");
                        final String response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(h, response, 200);
                        return;
                    }
                    if (path.equals("history")) {
                        System.out.println("Получаем Историю \nGet history");
                        final String response = gson.toJson(taskManager.getHistory());
                        sendText(h, response, 200);
                        return;
                    }
                    if (path.equals("task/")) {
                        if (query == null) {
                            System.out.println("Получаем все Задачи \nGet all Tasks");
                            final String response = gson.toJson(taskManager.getAllTasks());
                            sendText(h, response, 200);
                            return;
                        }
                        if (query.contains("=")) {
                            System.out.println("Получаем Задачу по id \nGet Task by id");
                            int id = parseId(query.split("=")[1]);
                            Task task = taskManager.getTaskById(id);
                            if (task == null) {
                                System.out.println("id не найден \nid Not Found");
                                sendText(h, "Not Found", 404);
                            } else {
                                final String response = gson.toJson(task);
                                sendText(h, response, 200);
                            }
                            return;
                        }
                    }
                    if (path.equals("subtask/")) {
                        if (query == null) {
                            System.out.println("Получаем все ПодЗадачи \nGet All SubTasks");
                            final String response = gson.toJson(taskManager.getAllSubtasks());
                            sendText(h, response, 200);
                            return;
                        }
                        if (query.contains("=")) {
                            System.out.println("Получаем ПодЗадачи по id \nGet SubTask by id");
                            int id = parseId(query.split("=")[1]);
                            SubTask subTask = taskManager.getSubTaskById(id);
                            if (subTask == null) {
                                System.out.println("id не найден \nid Not Found");
                                sendText(h, "Not Found", 404);
                            } else {
                                final String response = gson.toJson(subTask);
                                sendText(h, response, 200);
                            }
                            return;
                        }
                    }
                    if (path.equals("epic/")) {
                        if (query == null) {
                            System.out.println("Получаем все Эпики \nGet All Epics");
                            final String response = gson.toJson(taskManager.getAllEpics());
                            sendText(h, response, 200);
                            return;
                        }
                        if (query.contains("=")) {
                            System.out.println("Получаем Эпик по id \nGet Epic by id");
                            int id = parseId(query.split("=")[1]);
                            Epic epic = taskManager.getEpicById(id);
                            if (epic == null) {
                                System.out.println("id не найден \nid Not Found");
                                sendText(h, "Not Found", 404);
                            } else {
                                final String response = gson.toJson(epic);
                                sendText(h, response, 200);
                            }
                            return;
                        }
                    }
                    if (path.equals("subtask/epic/")) {
                        if (query.contains("=")) {
                            System.out.println("Получаем все ПодЗадачи Эпика по id \nGet All SubTasks Epic by id");
                            int id = parseId(query.split("=")[1]);
                            Epic epic = taskManager.getEpicById(id);
                            if (epic == null) {
                                System.out.println("id не найден \nid Not Found");
                                sendText(h, "Not Found", 404);
                            } else {
                                final String response = gson.toJson(taskManager.getAllSubtasksByEpicId(id));
                                sendText(h, response, 200);
                            }
                            return;
                        }
                    }
                    break;
                }

                case "POST": {
                    if (path.equals("task/")) {
                        String jsonBody = readText(h);
                        if (jsonBody.isEmpty()) {
                            System.out.println("Нет данных для выполнения запроса \n Insufficient data to complete the request");
                            sendText(h, "Insufficient data", 400);
                            return;
                        }
                        final Task task = gson.fromJson(jsonBody, Task.class);
                        final Integer id = task.getId();

                        if (id == null) {
                            System.out.println("Нет данных для выполнения запроса \n Insufficient data to complete the request");
                            sendText(h, "Insufficient data", 400);
                            return;
                        }
                        if (id == 0) {
                            taskManager.addTask(task);
                            System.out.println("Задача добавлена: " + task);
                            System.out.println("Task added: " + task);
                            final String response = gson.toJson(task);
                            sendText(h, response, 200);
                        } else {
                            taskManager.updateTask(task);
                            System.out.println("Задача обновлена: " + task);
                            System.out.println("Task updated: " + task);
                            final String response = gson.toJson(task);
                            sendText(h, response, 200);
                        }
                        return;
                    }
                    if (path.equals("subtask/")) {
                        final String jsonBody = readText(h);
                        if (jsonBody.isEmpty()) {
                            System.out.println("Нет данных для выполнения запроса \n Insufficient data to complete the request");
                            sendText(h, "Insufficient data", 400);
                            return;
                        }

                        final SubTask subTask = gson.fromJson(jsonBody, SubTask.class);
                        final Integer id = subTask.getId();
                        if (id == null) {
                            System.out.println("Нет данных для выполнения запроса \n Insufficient data to complete the request");
                            sendText(h, "Insufficient data", 400);
                            return;
                        }
                        if (id == 0) {
                            taskManager.addSubTask(subTask);
                            System.out.println("Подзадача добавлена: " + subTask);
                            System.out.println("SubTask added: " + subTask);
                            sendText(h, gson.toJson(subTask), 200);
                        } else {
                            taskManager.updateSubTask(subTask);
                            System.out.println("ПодЗадача обновлена: " + subTask);
                            System.out.println("SubTask updated: " + subTask);
                            sendText(h, gson.toJson(subTask), 200);
                        }
                        return;
                    }
                    if (path.equals("epic/")) {
                        String jsonBody = readText(h);
                        if (jsonBody.isEmpty()) {
                            System.out.println("Нет данных для выполнения запроса \n Insufficient data to complete the request");
                            sendText(h, "Insufficient data", 400);
                            return;
                        }

                        final Epic epic = gson.fromJson(jsonBody, Epic.class);
                        final Integer id = epic.getId();
                        if (id == null) {
                            System.out.println("Нет данных для выполнения запроса \n Insufficient data to complete the request");
                            sendText(h, "Insufficient data", 400);
                            return;
                        }
                        if (id == 0) {
                            taskManager.addEpic(epic);
                            System.out.println("Эпик добавлен: " + epic);
                            System.out.println("Epic added: " + epic);
                            sendText(h, gson.toJson(epic), 200);
                            return;
                        } else {
                            taskManager.updateEpic(epic);
                            System.out.println("Эпик обновлен: " + epic);
                            System.out.println("Epic updated: " + epic);
                            sendText(h, gson.toJson(epic), 200);
                            return;
                        }
                    }
                    break;
                }

                case "DELETE": {
                    if (path.equals("task/")) {
                        if (query == null) {
                            taskManager.deleteAllTasks();
                            System.out.println("Все задачи удалены \nAll Tasks removed");
                            final String response = "All Tasks have been deleted";
                            sendText(h, response, 200);
                            return;
                        } else {
                            if (query.contains("=")) {
                                System.out.println("Удаляем Задачи по id");
                                int id = parseId(query.split("=")[1]);
                                Task task = taskManager.getTaskById(id);
                                if (task == null) {
                                    System.out.println("id не найден \nid Not Found");
                                    sendText(h, "Not Found", 404);
                                } else {
                                    taskManager.deleteTaskById(id);
                                    final String response = "Task deleted";
                                    sendText(h, response, 200);
                                }
                                return;
                            }
                        }
                    }
                    if (path.equals("subtask/")) {
                        System.out.println("Удаляем ПодЗадачу по id \nDelete Subtask by id");
                        int id = parseId(query.split("=")[1]);
                        SubTask subTask = taskManager.getSubTaskById(id);
                        if (subTask == null) {
                            System.out.println("id не найден \nid Not Found");
                            sendText(h, "Not Found", 404);
                        } else {
                            taskManager.deleteSubtaskById(id);
                            final String response = "SubTask deleted";
                            sendText(h, response, 200);
                        }
                        return;
                    }
                    if (path.equals("epic/")) {
                        System.out.println("Удаляем Эпик по id \nDelete Epic by id");
                        int id = parseId(query.split("=")[1]);
                        Epic epic = taskManager.getEpicById(id);
                        if (epic == null) {
                            System.out.println("id не найден \nid Not Found");
                            sendText(h, "Not Found", 404);
                        } else {
                            taskManager.deleteEpicById(id);
                            final String response = "Epic deleted";
                            sendText(h, response, 200);
                        }
                        return;
                    }
                    break;
                }
                default: {
                    System.out.println("/ запрос " + requestMethod + " не обработан!");
                    System.out.println("/ request " + requestMethod + " not processed!");
                    h.sendResponseHeaders(400, 0);
                }
            }

        } catch (Exception exception) {
            System.out.println("Ошибка при обработке запроса");
            System.out.println("Error processing request");
        } finally {
            h.close();
        }
    }

    private void sendText(HttpExchange h, String responseText, int responseCode) throws IOException {
        byte[] responseInBytes = responseText.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(responseCode, responseInBytes.length);
        h.getResponseBody().write(responseInBytes);
    }

    private int parseId(String idString) {
        try {
            return Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }
}
