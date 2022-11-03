package test;

import main.constants.Status;
import main.manager.HistoryManager;
import main.manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.tasks.Task;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    private HistoryManager manager;
    private static int getIdCounter = 0;

    private int generateId() {
        return ++getIdCounter;
    }

    protected Task addTask() {

        return new Task("Title", "Description", Status.NEW, Instant.now(), 0);
    }

    @BeforeEach
    public void beforeEach() {

        manager = new InMemoryHistoryManager();
    }

    @Test
    public void addTasksToHistoryTest() {
        Task task1 = addTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = addTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = addTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        assertEquals(List.of(task1, task2, task3), manager.getHistory());
    }

    @Test
    public void removeTaskTest() {
        Task task1 = addTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = addTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = addTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task3.getId());
        assertEquals(List.of(task1, task2), manager.getHistory());
    }

    @Test
    public void removeOnlyOneTaskTest() {
        Task task = addTask();
        int newTaskId = generateId();
        task.setId(newTaskId);
        manager.add(task);
        manager.remove(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void historyIsEmptyTest() {
        Task task1 = addTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = addTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = addTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.remove(task1.getId());
        manager.remove(task2.getId());
        manager.remove(task3.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void notRemoveTaskWithBadIdTest() {
        Task task = addTask();
        int newTaskId = generateId();
        task.setId(newTaskId);
        manager.add(task);
        manager.remove(0);
        assertEquals(List.of(task), manager.getHistory());
    }
}