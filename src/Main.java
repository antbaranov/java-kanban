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


        System.out.println("\nСоздание Эпика 1 с 3-мя Подзадачами");
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

        System.out.println("Создание Подзадачи 3 Эпика 1");
        SubTask subTask3 = new SubTask("3 Наименование Подзадачи 3", "3 Описание Подзадачи 3", Status.NEW, epic1Id);
        int subTask3Id = taskManager.addSubTask(subTask3);
        System.out.println("id Подзадачи номер 3 subTask3Id: " + subTask3Id);


        taskManager.deleteByIdTask(task1Id);
        System.out.println("\nУдалённая задача 1: " + taskManager.getTaskById(task1Id));
        System.out.println("История просмотров: " + taskManager.getHistory());

        System.out.println("\nУдаляем Эпик 1 с 3-мя подзадачами");
        System.out.println("Имеем Эпик 1: " + taskManager.getEpicById(epic1Id));
        System.out.println("Подзадачи Эпика 1: " + taskManager.getSubTasksOfEpic(epic1Id));
        taskManager.deleteByIdEpic(epic1Id);
        System.out.println("Удаляем Эпик 1 с 3-мя подзадачами: " + taskManager.getEpicById(epic1Id));
        System.out.println("Удалённая Подзадача 1 Эпика 1: " + taskManager.getSubTaskById(subTask1Id));
        System.out.println("Удалённая Подзадача 2 Эпика 2: " + taskManager.getSubTaskById(subTask2Id));
        System.out.println("Удалённая Подзадача 3 Эпика 3: " + taskManager.getSubTaskById(subTask3Id));
        System.out.println("История просмотров: " + taskManager.getHistory());

    }
}
