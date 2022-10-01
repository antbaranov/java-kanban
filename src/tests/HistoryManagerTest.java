package tests;

import constants.TaskStatus;
import constants.Types;
import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest extends InMemoryHistoryManager {

    @Test
    void addHistoryAndRemoveHistoryAndGetHistoryTest() {
        HistoryManager manager = new InMemoryHistoryManager();
        Epic epic = new Epic(1, Types.EPIC, "new epic", TaskStatus.NEW, "test description");
        SubTask s1 = new SubTask(2, Types.SUBTASK, "subtask 1", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 20, 0), Duration.ofMinutes(30));
        SubTask s2 = new SubTask(3, Types.SUBTASK, "subtask 2", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30));
        SubTask s3 = new SubTask(4, Types.SUBTASK, "subtask 3", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 19, 0), Duration.ofMinutes(45));
        manager.addHistory(epic);
        manager.addHistory(s1);
        manager.addHistory(s2);
        manager.addHistory(s3);
        assertEquals(4, manager.getHistory().size());

        manager.remove(1);

        assertEquals(3, manager.getHistory().size());
        assertFalse(manager.getHistory().contains(epic));

        manager.remove(4);

        assertEquals(2, manager.getHistory().size());
        assertFalse(manager.getHistory().contains(s3));

        manager.addHistory(epic);
        manager.addHistory(s3);
        manager.remove(4);
        manager.remove(3);

        assertEquals(1, manager.getHistory().size());
        assertFalse(manager.getHistory().contains(s2));

    }

    @Test
    void emptyHistoryTest() {
        HistoryManager manager = new InMemoryHistoryManager();
        Assertions.assertEquals(0, manager.getHistory().size());
    }

    @Test
    void duplicateInHistoryTest() {
        HistoryManager manager = new InMemoryHistoryManager();
        Epic epic = new Epic(1, Types.EPIC, "new epic", TaskStatus.NEW, "test description");

        manager.addHistory(epic);
        manager.addHistory(epic);
        manager.addHistory(epic);
        manager.addHistory(epic);

        assertEquals(1, manager.getHistory().size());
    }
}