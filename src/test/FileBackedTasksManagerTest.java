package test;

import main.constants.Status;
import main.manager.FileBackedTasksManager;
import main.manager.InMemoryTaskManager;
import main.manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import main.tasks.Epic;
import main.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public static final Path PATH = Path.of("test_tasks_file.csv");
    File file = new File(String.valueOf(PATH));

    @Override
    public InMemoryTaskManager createManager() {

        manager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);
        return manager;
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
    public void saveAndLoadTest() {
        Task task = new Task("Title", "Description", Status.NEW, Instant.now(), 0);
        manager.addTask(task);
        Epic epic = new Epic("Title", "Description", Status.NEW, Instant.now(), 0);
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