import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();

        System.out.println("Создание простой задачи");
        Task task = new Task(0, "1 Наименование простой задачи 1", "1 Описание простой задачи 1", "NEW");
        manager.creatTask(task);
        task = new Task(0, "2 Наименование простой задачи 2", "2 Описание простой задачи 2", "NEW");
        manager.creatTask(task);
        System.out.println(manager.printTasks());


        System.out.println("Создание Эпика 1");
        Epic epic = new Epic(0, "1 Наименование Эпик 1", "1 Описание Эпик 1", "NEW");
        manager.creatEpic(epic);
        System.out.println("Создание Подзадачи");
        SubTask subTask = new SubTask(0, "1 Наименование Подзадачи 1", "1 Описание Подзадачи 1", "NEW", 0);
        manager.creatSubTask(subTask);
        subTask =  new SubTask(0, "2 Наименование Подзадачи 2", "2 Описание Подзадачи 2", "NEW", 0);
        manager.creatSubTask(subTask);


        System.out.println("Создание Эпика 2");
        // Epic(int id, String name, String description, String status)
        epic = new Epic(0, "2 Наименование Эпик 2", "2 Описание Эпик 2", "NEW");
        manager.creatEpic(epic);

        System.out.println("Создание Подзадачи для 2 Эпика");
        // SubTask(int id, String name, String description, String status, int epicId)
        subTask = new SubTask(0, "1 Наименование Подзадачи 1", "1 Описание Подзадачи 1", "NEW", 1);
        manager.creatSubTask(subTask);
        System.out.println(manager.printEpics());
        System.out.println(manager.printSubTasks());

        // System.out.println(manager.deleteTasks() + " Список должен быть пустой: " + manager.printTasks());

        System.out.println(manager.getByIdTask(1));


        System.out.println("Обновление простой задачи");
        Task taskUpdate = new Task(1,"Обновили Наименования Задачи", "Обновили Описание Задачи", "IN_PROGRESS");
        taskUpdate.setId(1);
        manager.updateTask(taskUpdate);
        System.out.println("Список обновленных задач: " + manager.printTasks());


        //   System.out.println(manager.deleteByIdTask(1));
    }
}
