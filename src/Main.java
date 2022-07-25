public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager(taskId, taskName, taskDescription, taskStatus);

        System.out.println("Создание задачи");
        System.out.println("Введите название задачи");
        String taskName = new nextline();
        String taskDescription = new nextLine();
        taskManager.creatTask(taskName, taskDescription, taskStatus)

    }
}
