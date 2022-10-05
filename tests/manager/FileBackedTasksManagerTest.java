import constants.Status;
import manager.FileBackedTasksManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

 class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public static final Path path = Path.of("data_test.csv");
    File file = new File(String.valueOf(path));
    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(path);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void saveAndLoadTest(){
        Task task = new Task( "Title", "Description", Status.NEW, Instant.now(), 0);
        manager.addTask(task);
        Epic epic = new Epic( "Title", "Description", Status.NEW, Instant.now(), 0);
        manager.addEpic(epic);
        FileBackedTasksManager fileManager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);
        fileManager.loadFromFile();
        Assertions.assertEquals(List.of(task), manager.getAllTasks());
        Assertions.assertEquals(List.of(epic), manager.getAllEpics());
    }

    @Test
    public void saveAndLoadEmptyTasksEpicsSubtasksTest() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);
        fileManager.save();
        fileManager.loadFromFile();
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getAllTasks());
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getAllEpics());
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getAllSubtasks());
    }

    @Test
    public void saveAndLoadEmptyHistoryTest() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);
        fileManager.save();
        fileManager.loadFromFile();
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }
}