package server;

import com.sun.net.httpserver.HttpServer;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import server.http_handlers.HistoryHandler;
import server.http_handlers.SubtaskHandler;
import server.http_handlers.TaskHandler;
import server.http_handlers.TasksHandler;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException, InterruptedException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault(historyManager);
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/main/tasks/", new TasksHandler(taskManager));
        httpServer.createContext("/main/tasks/task/", new TaskHandler(taskManager));
        httpServer.createContext("/main/tasks/epic/", new main.server.http_handlers.EpicHandler(taskManager));
        httpServer.createContext("/main/tasks/subtask/", new SubtaskHandler(taskManager));
        httpServer.createContext("/main/tasks/subtask/epic/", new main.server.http_handlers.SubtaskByEpicHandler(taskManager));
        httpServer.createContext("/main/tasks/history/", new HistoryHandler(taskManager));
    }

    public void start() {
        System.out.println("HTTP-cервер запущен на " + PORT + " порту!");
        System.out.println("HTTP server running on " + PORT + " port!");
        System.out.println("http://localhost:" + PORT + "/main/tasks/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("HTTP-cервер остановлен на " + PORT + " порту!");
        System.out.println("HTTP server stopped on " + PORT + " port!");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new HttpTaskServer().start();
    }

}