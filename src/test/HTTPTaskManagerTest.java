import main.constants.Status;
import manager.HistoryManager;
import main.manager.Managers;
import main.server.HTTPTaskManager;
import main.server.KVServer;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// class test.HTTPTaskManagerTest {
class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {


    private KVServer server;
    HTTPTaskManager manager;


    @Override
    public HTTPTaskManager createManager() {
        try {
            server = new KVServer();
            server.start();
            HistoryManager historyManager = Managers.getDefaultHistory();
            manager = Managers.getDefault(historyManager);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при создании менеджера");
        }
        return manager;
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void loadTasksTest() {
        Task task1 = new Task("Title Task 1", "Description Task 1", Status.NEW, Instant.now(), 15);
        Task task2 = new Task("Title Task 2", "Description Task 2", Status.NEW, Instant.now(), 15);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllTasks(), list);
    }

    @Test
    public void loadEpicsTest() {
        Epic epic1 = new Epic("Title Epic 1", "Description Epic 1", Status.NEW, Instant.now(), 15);
        Epic epic2 = new Epic("Title Epic 2", "Description Epic 2", Status.NEW, Instant.now(), 15);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllEpics(), list);
    }

    @Test
    public void loadSubTasksTest() {
        Epic epic1 = new Epic("Title Epic 1", "Description Epic 1", Status.NEW, Instant.now(), 30);
        SubTask subTask1 = new SubTask("Title SubTask 1", "Description SubTask 1", Status.NEW, epic1.getId()
                , Instant.now(), 15);
        SubTask SubTask2 = new SubTask("Title SubTask 2", "Description SubTask 2", Status.NEW, epic1.getId(),
                Instant.now(), 15);
        manager.addSubTask(subTask1);
        manager.addSubTask(SubTask2);
        manager.getSubTaskById(subTask1.getId());
        manager.getSubTaskById(SubTask2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllSubtasks(), list);
    }

}