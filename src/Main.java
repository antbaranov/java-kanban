import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        System.out.println("Создание задачи");
        System.out.println("Введите название задачи");
        /*String taskName = new scanner.nextline();
        String taskDescription = new scanner.nextLine();*/
        taskManager.creatTask("Переезд", "Описание переезда", "NEW");
        System.out.println(taskManager.tasks);
    }
}
