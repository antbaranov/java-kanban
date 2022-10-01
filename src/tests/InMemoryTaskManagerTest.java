package tests;

import constants.TaskStatus;
import constants.Types;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    protected InMemoryTaskManager createManager() {
        InMemoryTaskManager.setIdCounter(1);
        return new InMemoryTaskManager();
    }

    @Test
    void getIdCounterTest() {
        manager.addTask(new Task(1, Types.TASK.TASK, "Task1", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.addTask(new Task(2, Types.TASK, "Task2", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), Duration.ofMinutes(30)));
        manager.addEpic(new Epic(3, Types.EPIC, "new epic", TaskStatus.NEW, "test description"));

        assertEquals(4, InMemoryTaskManager.getIdCounter());
    }

    @Test
    void setIdCounterTest() {
        InMemoryTaskManager.setIdCounter(8);

        assertEquals(8, InMemoryTaskManager.getIdCounter());
    }
}