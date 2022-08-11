import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefaultTask();

        System.out.println("\nСоздание простой задачи");
        Task task1 = new Task("1 Наименование простой задачи 1", "1 Описание простой задачи 1", Status.NEW);
        int task1Id = taskManager.addTask(task1);
        System.out.println("id простой задачи номер 1 task1Id: " + task1Id);
        Task task2 = new Task("2 Наименование простой задачи 2", "2 Описание простой задачи 2", Status.NEW);
        int task2Id = taskManager.addTask(task2);
        System.out.println("id простой задачи номер 2 task2Id: " + task2Id);
        Task task3 = new Task("3 Наименование простой задачи 3", "3 Описание простой задачи 3", Status.NEW);
        int task3Id = taskManager.addTask(task3);
        System.out.println("id простой задачи номер 3 task3Id: " + task3Id);

        System.out.println("\nСоздание Эпика 1 с 2-мя Подзадачами");
        Epic epic1 = new Epic("1 Наименование Эпик 1", "1 Описание Эпик 1", Status.NEW);
        int epic1Id = taskManager.addEpic(epic1);
        System.out.println("id Эпика номер 1 epic1Id: " + epic1Id);

        System.out.println("Создание Подзадачи 1 Эпика 1");
        SubTask subTask1 = new SubTask("1 Наименование Подзадачи 1", "1 Описание Подзадачи 1", Status.NEW, epic1Id);
        int subTask1Id = taskManager.addSubTask(subTask1);
        System.out.println("id Подзадачи номер 1 subTask1Id: " + subTask1Id);

        System.out.println("Создание Подзадачи 2 Эпика 1");
        SubTask subTask2 = new SubTask("2 Наименование Подзадачи 2", "2 Описание Подзадачи 2", Status.NEW, epic1Id);
        int subTask2Id = taskManager.addSubTask(subTask2);
        System.out.println("id Подзадачи номер 2 subTask2Id: " + subTask2Id);

        System.out.println("\nСоздание Эпика 2 с 1-ой Подзадачей");
        Epic epic2 = new Epic("2 Наименование Эпик 2", "2 Описание Эпик 2", Status.NEW);
        int epic2Id = taskManager.addEpic(epic2);
        System.out.println("id Эпика номер 2 epic2Id: " + epic2Id);

        System.out.println("Создание Подзадачи 3 для 2 Эпика");
        SubTask subTask3 = new SubTask("Наименование Подзадачи 3", "Описание Подзадачи 3", Status.NEW, epic2Id);
        int subTask3Id = taskManager.addSubTask(subTask3);
        System.out.println("id Подзадачи номер 3 subTask3Id: " + subTask3Id);

        System.out.println("\nВывод списков Эпиков, Задач, Подзадач");
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubTask());

        System.out.println("\nВызов методов");
        System.out.println(taskManager.getTaskById(task2Id));
        System.out.println(taskManager.getHistory());

        System.out.println("\nОбновление простой задачи");
        Task taskUpdate = new Task("Обновили Наименования Задачи 1", "Обновили Описание Задачи 1", Status.IN_PROGRESS);
        taskUpdate.setId(1);
        taskManager.updateTask(taskUpdate);
        taskManager.updateTask(new Task("Обновили Наименования Задачи 2", "Обновили Описание Задачи 2", Status.DONE));
        System.out.println(taskManager.getHistory());

        System.out.println("\nОбновление подзадачи");
        taskManager.updateSubTask(new SubTask(
                "Обновили Наименование Подзадачи 1 Эпик 1", "Обновили Описание Подзадачи 1",
                Status.IN_PROGRESS, epic1Id));
        taskManager.updateSubTask(new SubTask(
                "Обновили Наименование Подзадачи 2 Эпик 1", "Обновили Описание Подзадачи 1",
                Status.DONE, epic1Id));
        taskManager.updateSubTask(new SubTask(
                "Обновили Наименование Подзадачи 1 Эпик 2", "Обновили Описание Подзадачи 1",
                Status.DONE, epic2Id));
        System.out.println(taskManager.getHistory());

        taskManager.getTaskById(task2Id);
        System.out.println(taskManager.getHistory());

        taskManager.getEpicById(epic1Id);
        System.out.println(taskManager.getHistory());

        taskManager.getSubTaskById(subTask2Id);
        System.out.println(taskManager.getHistory());
    }
}
