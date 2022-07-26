import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();

        System.out.println("Создание задачи");
        manager.creatTask("1 Наименование задачи 1", "1 Описание задачи 1", "NEW");
        manager.creatTask("2 Наименование задачи 2", "2 Описание задачи 2", "NEW");
        System.out.println(manager.printTasks());

        System.out.println("Создание Подзадачи");
        manager.creatSubTask("1 Наименование Подзадачи 1", "1 Описание Подзадачи 1", "NEW");
        manager.creatSubTask("2 Наименование Подзадачи 2", "2 Описание Подзадачи 2", "NEW");
        System.out.println(manager.printSubTasks());


        System.out.println("Создание Эпика");
        manager.creatEpic("1 Наименование Эпик 1", "1 Описание Эпик 1", "NEW");
        manager.creatEpic("2 Наименование Эпик 2", "2 Описание Эпик 2", "NEW");
        System.out.println(manager.printEpics());
        // System.out.println(manager.deleteTasks() + " Список должен быть пустой: " + manager.printTasks());

        System.out.println(manager.getByIdTask(1));

     //   System.out.println(manager.updateTask(1, "Обновление Наименования Задачи", "Обновление Описание Задачи", "IN_PROGRESS"));

     //   System.out.println(manager.deleteByIdTask(1));
    }
}
