import constants.TaskStatus;
import constants.Types;
import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefaultTask();
        FileBackedTasksManager managerFile = Managers.getDefaultFileManager();

        System.out.println("\nСоздание простой задачи");
        Task task1 = new Task(
                Types.TASK,
                "1 Наименование простой задачи 1",
                "1 Описание простой задачи 1",
                TaskStatus.NEW,
                LocalDateTime.of(2022, 10, 1, 0, 0),
                Duration.ofMinutes(45)
        );
        int task1Id = taskManager.addTask(task1);

        System.out.println("\nСоздание Эпика 1 с 3-мя Подзадачами");
        Epic epic1 = new Epic("1 Наименование Эпик 1", "1 Описание Эпик 1", TaskStatus.NEW);
        int epic1Id = taskManager.addEpic(epic1);
        System.out.println("id Эпика номер 1 epic1Id: " + epic1Id);

        System.out.println("Создание Подзадачи 1 Эпика 1");
        SubTask subTask1 = new SubTask(
                "1 Наименование Подзадачи 1",
                "1 Описание Подзадачи 1",
                TaskStatus.NEW,
                epic1Id,
                LocalDateTime.of(2022, 10, 1, 9, 0),
                Duration.ofMinutes(60 + 10)
        );
        int subTask1Id = taskManager.addSubTask(subTask1);
        System.out.println("id Подзадачи номер 1 subTask1Id: " + subTask1Id);

        System.out.println("Создание Подзадачи 2 Эпика 1");
        SubTask subTask2 = new SubTask(
                "2 Наименование Подзадачи 2",
                "2 Описание Подзадачи 2",
                TaskStatus.NEW,
                epic1Id,
                LocalDateTime.of(2022, 10, 1, 10, 25),
                Duration.ofMinutes(7)
        );
        int subTask2Id = taskManager.addSubTask(subTask2);
        System.out.println("id Подзадачи номер 2 subTask2Id: " + subTask2Id);

        System.out.println("Создание Подзадачи 3 Эпика 1");
        SubTask subTask3 = new SubTask(
                "3 Наименование Подзадачи 3",
                "3 Описание Подзадачи 3",
                TaskStatus.NEW,
                epic1Id,
                LocalDateTime.of(2022, 10, 2, 12, 25),
                Duration.ofMinutes(3 * 60 + 25)
        );
        int subTask3Id = taskManager.addSubTask(subTask3);
        System.out.println("id Подзадачи номер 3 subTask3Id: " + subTask3Id);

        managerFile.loadFromFile();

    }
}
