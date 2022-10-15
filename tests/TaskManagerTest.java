import constants.Status;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager>  {


    protected T manager;
    protected Task addTask() {

        return new Task( "Title", "Description", Status.NEW, Instant.now(), 0);
    }
    protected Epic addEpic() {

        return new Epic("Title", "Description",  Status.NEW, Instant.now(), 0);
    }
    protected SubTask addSubTask(Epic epic) {
        return new SubTask("Title", "Description", Status.NEW, epic.getId(), Instant.now(), 0);
    }

    @Test
    public void addTaskTest() {
        Task task = addTask();
        manager.addTask(task);
        List<Task> tasks = manager.getAllTasks();
        assertNotNull(task.getStatus());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(List.of(task), tasks);
    }

    @Test
    public void addEpicTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        List<Epic> epics = manager.getAllEpics();
        assertNotNull(epic.getStatus());
        assertEquals(Status.NEW, epic.getStatus());
        assertEquals(Collections.EMPTY_LIST, epic.getSubtaskIds());
        assertEquals(List.of(epic), epics);
    }

    @Test
    public void addSubTaskTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        List<SubTask> SubTasks = manager.getAllSubtasks();
        assertNotNull(SubTask.getStatus());
        assertEquals(epic.getId(), SubTask.getEpicId());
        assertEquals(Status.NEW, SubTask.getStatus());
        assertEquals(List.of(SubTask), SubTasks);
        assertEquals(List.of(SubTask.getId()), epic.getSubtaskIds());
    }


    @Test
    void taskNullTest() {
        Task task = manager.addTask(null);
        assertNull(task);
    }

    @Test
    void epicNullTest() {
        Epic epic = manager.addEpic(null);
        assertNull(epic);
    }

    @Test
    void subTaskNullTest() {
        SubTask SubTask = manager.addSubTask(null);
        assertNull(SubTask);
    }

    @Test
    public void updateTaskStatusToInProgressTest() {
        Task task = addTask();
        manager.addTask(task);
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(task.getId()).getStatus());
    }

    @Test
    public void updateEpicStatusToInProgressTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    public void updateSubTaskStatusToInProgressTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        SubTask.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(SubTask);
        assertEquals(Status.IN_PROGRESS, manager.getSubTaskById(SubTask.getId()).getStatus());
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    public void updateTaskStatusToInDoneTest() {
        Task task = addTask();
        manager.addTask(task);
        task.setStatus(Status.DONE);
        manager.updateTask(task);
        assertEquals(Status.DONE, manager.getTaskById(task.getId()).getStatus());
    }

    @Test
    public void updateEpicStatusToInDoneTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        epic.setStatus(Status.DONE);
        assertEquals(Status.DONE, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    public void updateSubTaskStatusToInDoneTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        SubTask.setStatus(Status.DONE);
        manager.updateSubTask(SubTask);
        assertEquals(Status.DONE, manager.getSubTaskById(SubTask.getId()).getStatus());
        assertEquals(Status.DONE, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    public void notUpdateTaskIfNullTest() {
        Task task = addTask();
        manager.addTask(task);
        manager.updateTask(null);
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    public void notUpdateEpicIfNullTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.updateEpic(null);
        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    public void notUpdateSubTaskIfNullTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        manager.updateSubTask(null);
        assertEquals(SubTask, manager.getSubTaskById(SubTask.getId()));
    }

    @Test
    public void deleteAllTasksTest() {
        Task task = addTask();
        manager.addTask(task);
        manager.deleteAllTasks();
        assertEquals(Collections.EMPTY_LIST, manager.getAllTasks());
    }

    @Test
    public void deleteAllEpicsTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.deleteAllEpics();
        assertEquals(Collections.EMPTY_LIST, manager.getAllEpics());
    }

    @Test
    public void deleteAllSubTasksTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        manager.deleteAllSubtasks();
        assertTrue(epic.getSubtaskIds().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void deleteAllSubTasksByEpicTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        manager.deleteAllSubtasksByEpic(epic);
        assertTrue(epic.getSubtaskIds().isEmpty());
        assertEquals(true, manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void deleteTaskByIdTest() {
        Task task = addTask();
        manager.addTask(task);
        manager.deleteTaskById(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getAllTasks());
    }

    @Test
    public void deleteEpicById() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.deleteEpicById(epic.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getAllEpics());
    }

    @Test
    public void notDeleteTaskIfBadIdTest() {
        Task task = addTask();
        manager.addTask(task);
        manager.deleteTaskById(-1);
        assertEquals(List.of(task), manager.getAllTasks());
    }

    @Test
    public void notDeleteEpicIfBadIdTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        manager.deleteEpicById(-1);
        assertEquals(List.of(epic), manager.getAllEpics());
    }

    @Test
    public void notDeleteSubTaskIfBadIdTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        manager.deleteSubtaskById(-1);
        assertEquals(List.of(SubTask), manager.getAllSubtasks());
        assertEquals(List.of(SubTask.getId()), manager.getEpicById(epic.getId()).getSubtaskIds());
    }

    @Test
    public void doNothingIfTaskHashMapIsEmptyTest(){
        manager.deleteAllTasks();
        manager.deleteTaskById(-1);
        assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    public void doNothingIfEpicHashMapIsEmptyTest(){
        manager.deleteAllEpics();
        manager.deleteEpicById(-1);
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void doNothingIfSubTaskHashMapIsEmptyTest(){
        manager.deleteAllEpics();
        manager.deleteSubtaskById(-1);
        assertEquals(0, manager.getAllSubtasks().size());
    }

    @Test
    void returnEmptyListWhenGetSubTaskByEpicIdIsEmptyTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        List<SubTask> SubTasks = manager.getAllSubtasksByEpicId(epic.getId());
        assertTrue(SubTasks.isEmpty());
    }

    @Test
    public void returnEmptyListTasksIfNoTasksTest() {
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void returnEmptyListEpicsIfNoEpicsTest() {
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void returnEmptyListSubTasksIfNoSubTasksTest() {
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void returnNullIfTaskDoesNotExist() {
        assertNull(manager.getTaskById(-1));
    }

    @Test
    public void returnNullIfEpicDoesNotExist() {
        assertNull(manager.getEpicById(-1));
    }

    @Test
    public void returnNullIfSubtaskDoesNotExist() {
        assertNull(manager.getSubTaskById(-1));
    }

    @Test
    public void returnEmptyHistoryTest() {
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void returnEmptyHistoryIfTasksNotExist() {
        manager.getTaskById(-1);
        manager.getSubTaskById(-1);
        manager.getEpicById(-1);
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void returnHistoryTasksTest() {
        Epic epic = addEpic();
        manager.addEpic(epic);
        SubTask SubTask = addSubTask(epic);
        manager.addSubTask(SubTask);
        manager.getEpicById(epic.getId());
        manager.getSubTaskById(SubTask.getId());
        List<Task> list = manager.getHistory();
        assertEquals(2, list.size());
        assertTrue(list.contains(SubTask));
        assertTrue(list.contains(epic));
    }

}