package tests;

import constants.TaskStatus;
import constants.Types;
import exceptions.ManagerSaveException;
import manager.FileBackedTasksManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @Override
    protected FileBackedTasksManager createManager() {
        FileBackedTasksManager.setIdCounter(1);
        return new FileBackedTasksManager();
    }

    // падает

    @Test
    void loadedFromFileTasksManagerTest() {
        Epic epic = new Epic(1, Types.EPIC, "new epic", TaskStatus.NEW, "test description");
        SubTask s1 = new SubTask(2, Types.SUBTASK, "subtask 1", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 20, 0), Duration.ofMinutes(30));
        SubTask s2 = new SubTask(3, Types.SUBTASK, "subtask 2", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        SubTask s3 = new SubTask(4, Types.SUBTASK, "subtask 3", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 19, 0), Duration.ofMinutes(45));

        manager.addEpic(epic);
        manager.addSubTask(s1);
        manager.addSubTask(s2);
        manager.addSubTask(s3);

        manager.getEpicById(1);
        manager.getSubTaskById(4);
        manager.getEpicById(1);
        manager.getSubTaskById(2);


        String[] lines;
        final String PATH = "resources/data.csv";
        try {
            lines = Files.readString(Path.of(PATH)).split("\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать файл");
        }
        assertEquals(7, lines.length);

        FileBackedTasksManager fileManagerFromFile = Managers.getDefaultFileManager();


        fileManagerFromFile.loadFromFile();

        Integer[] forComparison = {s3.getId(), epic.getId(), s1.getId()};
        List<Task> history = fileManagerFromFile.getHistory();
        Integer[] historyFromFile = {history.get(0).getId(), history.get(1).getId(), history.get(2).getId()};

        assertEquals("new epic", fileManagerFromFile.getEpics().get(1).getName());
        assertEquals("subtask 1", fileManagerFromFile.getSubTask().get(2).getName());
        assertEquals("subtask 2", fileManagerFromFile.getSubTask().get(3).getName());
        assertEquals("subtask 3", fileManagerFromFile.getSubTask().get(4).getName());
        assertArrayEquals(forComparison, historyFromFile);
    }
}