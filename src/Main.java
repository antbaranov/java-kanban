import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        System.out.println("Создание задачи");
        /*System.out.println("Введите название задачи");
        String taskName = scanner.nextLine();
        System.out.println("Введите описание задачи");
        String taskDescription = scanner.nextLine();*/
        taskManager.creatTask("1 Наименование задачи 1", "1 Описание задачи 1", "NEW");
        taskManager.creatTask("2 Наименование задачи 2", "2 Описание задачи 2", "NEW");
        System.out.println(taskManager.tasks);
        for (Task value : taskManager.tasks.values()) {
            System.out.println(value);
        }
        System.out.println("Создание Подзадачи");
        /*System.out.println("Введите название задачи");
        String taskName = scanner.nextLine();
        System.out.println("Введите описание задачи");
        String taskDescription = scanner.nextLine();*/
        taskManager.creatSubTask("1 Наименование Подзадачи 1", "1 Описание Подзадачи 1", "NEW");
        taskManager.creatSubTask("2 Наименование Подзадачи 2", "2 Описание Подзадачи 2", "NEW");
        System.out.println(taskManager.subTasks);
        for (SubTask value : taskManager.subTasks.values()) {
            System.out.println(value);
        }

        System.out.println("Создание Эпика");
        taskManager.creatEpic("1 Наименование Эпик 1", "1 Описание Эпик 1", "NEW");
        taskManager.creatEpic("2 Наименование Эпик 2", "2 Описание Эпик 2", "NEW");
        System.out.println(taskManager.epics);
        for (Epic value : taskManager.epics.values()) {
            System.out.println(value);
        }

    }
}
