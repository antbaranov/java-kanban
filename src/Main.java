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
        Task task1 = new Task(1, "1 Наименование простой задачи 1", "1 Описание простой задачи 1", Status.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task(2, "2 Наименование простой задачи 2", "2 Описание простой задачи 2", Status.NEW);
        taskManager.addTask(task2);
        Task task3 = new Task(3, "3 Наименование простой задачи 3", "3 Описание простой задачи 3", Status.NEW);
        taskManager.addTask(task3);

        System.out.println("\nСоздание Эпика 1 с 2-мя Подзадачами");
        Epic epic1 = new Epic(1, "1 Наименование Эпик 1", "1 Описание Эпик 1", Status.NEW);
        taskManager.addEpic(epic1);
        System.out.println("Создание Подзадачи 1 Эпика 1");
        SubTask subTask1 = new SubTask(1, "1 Наименование Подзадачи 1", "1 Описание Подзадачи 1", Status.NEW, epic1.getId());
        taskManager.addSubTask(subTask1);
        System.out.println("Создание Подзадачи 2 Эпика 1");
        SubTask subTask2 = new SubTask(2, "2 Наименование Подзадачи 2", "2 Описание Подзадачи 2", Status.NEW, epic1.getId());
        taskManager.addSubTask(subTask2);

        System.out.println("\nСоздание Эпика 2");
        Epic epic2 = new Epic(2, "2 Наименование Эпик 2", "2 Описание Эпик 2", Status.NEW);
        taskManager.addEpic(epic2);

        System.out.println("Создание Подзадачи для 2 Эпика");
        SubTask subTask3 = new SubTask(3, "Наименование Подзадачи 3", "Описание Подзадачи 3", Status.NEW, epic2.getId());
        taskManager.addSubTask(subTask3);

        System.out.println("\nВывод списков Эпиков, Задач, Подзадач");
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubTask());

        System.out.println("\nВызов методов");
        System.out.println(taskManager.getByIdTask(1));
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getByIdEpic(1));
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getByIdSubTask(1));
        System.out.println(taskManager.getHistory());

        System.out.println("\nОбновление простой задачи");
        Task taskUpdate = new Task(1, "Обновили Наименования Задачи 1", "Обновили Описание Задачи 1", Status.IN_PROGRESS);
        taskUpdate.setId(1);
        taskManager.updateTask(taskUpdate);
        taskManager.updateTask(new Task(2, "Обновили Наименования Задачи 2", "Обновили Описание Задачи 2", Status.DONE));
        System.out.println(taskManager.getHistory());

        System.out.println("\nОбновление подзадачи");
        taskManager.updateSubTask(new SubTask(3, "Обновили Наименование Подзадачи 1 Эпик 1", "Обновили Описание Подзадачи 1", Status.IN_PROGRESS, epic1.getId()));
        taskManager.updateSubTask(new SubTask(4, "Обновили Наименование Подзадачи 2 Эпик 1", "Обновили Описание Подзадачи 1", Status.DONE, epic1.getId()));
        taskManager.updateSubTask(new SubTask(5, "Обновили Наименование Подзадачи 1 Эпик 2", "Обновили Описание Подзадачи 1", Status.DONE, epic2.getId()));
        System.out.println(taskManager.getHistory());

        taskManager.getByIdTask(task2.getId());
        System.out.println(taskManager.getHistory());

        taskManager.getByIdEpic(epic1.getId());
        System.out.println(taskManager.getHistory());

        taskManager.getByIdSubTask(subTask2.getId());
        System.out.println(taskManager.getHistory());

    }
}
