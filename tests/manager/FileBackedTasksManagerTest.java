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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

 class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public static final Path PATH = Path.of("test_tasks_file.csv");
    File file = new File(String.valueOf(PATH));
    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(PATH);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void saveAndLoadTest(){
        Task task = new Task( "Title", "Description", Status.NEW, LocalDateTime.now(), 0);
        manager.addTask(task);
        Epic epic = new Epic( "Title", "Description", Status.NEW, LocalDateTime.now(), 0);
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