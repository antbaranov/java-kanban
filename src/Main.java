/*
Привет! В проекте реализовал то, что понимаю как сделать. Остальные задачи нет понимания как делать.
Вопросы наставнику задавал, но мы разговариваем на разных языках и не понимаем друг друга. Поэтому проект отправляю в таком виде.
*/

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();

        System.out.println("\nСоздание простой задачи");
        Task task1 = new Task(0, "1 Наименование простой задачи 1", "1 Описание простой задачи 1", "NEW");
        manager.creatTask(task1);
        Task task2 = new Task(0, "2 Наименование простой задачи 2", "2 Описание простой задачи 2", "NEW");
        manager.creatTask(task2);
        System.out.println(manager.printTasks());


        System.out.println("\nСоздание Эпика 1 с 2-мя Подзадачами");
        Epic epic1 = new Epic(1, "1 Наименование Эпик 1", "1 Описание Эпик 1", "NEW");
        manager.creatEpic(epic1);
        System.out.println("Создание Подзадачи 1 Эпика 1");
        SubTask subTask1 = new SubTask(0, "1 Наименование Подзадачи 1", "1 Описание Подзадачи 1", "NEW", epic1.getId());
        manager.creatSubTask(subTask1);
        System.out.println("Создание Подзадачи 2 Эпика 1");
        SubTask subTask2 =  new SubTask(0, "2 Наименование Подзадачи 2", "2 Описание Подзадачи 2", "NEW", epic1.getId());
        manager.creatSubTask(subTask2);
        System.out.println(manager.printEpics());


        System.out.println("\nСоздание Эпика 2");
        // Epic(int id, String name, String description, String status)
        Epic epic2 = new Epic(1, "2 Наименование Эпик 2", "2 Описание Эпик 2", "NEW");
        manager.creatEpic(epic2);

        System.out.println("Создание Подзадачи для 2 Эпика");
        // SubTask(int id, String name, String description, String status, int epicId)
        SubTask subTask3 = new SubTask(1, "1 Наименование Подзадачи 1", "1 Описание Подзадачи 1", "NEW", epic2.getId());
        manager.creatSubTask(subTask3);
        System.out.println(manager.printEpics());
        System.out.println(manager.printSubTasks());



        System.out.println(manager.getByIdTask(1));


        System.out.println("\nОбновление простой задачи");
        Task taskUpdate = new Task(1,"Обновили Наименования Задачи 1", "Обновили Описание Задачи 1", "IN_PROGRESS");
        taskUpdate.setId(1);
        manager.updateTask(taskUpdate);
        manager.updateTask(new Task(2,"Обновили Наименования Задачи 2", "Обновили Описание Задачи 2", "DONE"));
        System.out.println("Список обновленных задач: " + manager.printTasks());

        System.out.println("\nОбновление подзадачи");
        manager.updateSubTask(new SubTask(3, "Обновили Наименование Подзадачи 1 Эпик 1", "1 Описание Подзадачи 1", "IN_PROGRESS", epic1.getId()));
        manager.updateSubTask(new SubTask(4, "Обновили Наименование Подзадачи 2 Эпик 1", "1 Описание Подзадачи 1", "DONE", epic1.getId()));
        manager.updateSubTask(new SubTask(5, "Обновили Наименование Подзадачи 1 Эпик 2", "1 Описание Подзадачи 1", "DONE", epic2.getId()));

        System.out.println(manager.printEpics());
        System.out.println(manager.printSubTasks());

        System.out.println("\nСписок Эпиков и его подзадач после удаления Эпика");
        System.out.println(manager.deleteByIdEpic(3));
        System.out.println(manager.printEpics());
        System.out.println(manager.printSubTasks());

        System.out.println("\nУдаление подзадачи");
        manager.deleteByIdSubTask(3);
        System.out.println(manager.printEpics());
        System.out.println(manager.printSubTasks());


         System.out.println(manager.deleteTasks() + " Список должен быть пустой: " + manager.printTasks());
        //   System.out.println(manager.deleteByIdTask(1));
    }
}
