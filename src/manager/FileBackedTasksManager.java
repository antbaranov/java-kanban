package manager;

import tasks.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {




    public FileBackedTasksManager() {
    }

    String fileName = "file_data.csv";

    // Метод сохраняет текущее состояние менеджера в указанный файл
    public void save() {
        try (FileWriter fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic" + "\n"); // Запись шапки с заголовками в файл
            for (Task task : getTasks()) {
                fileWriter.write(taskToString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                fileWriter.write(epicToString(epic) + "\n");
            }
            for (SubTask subTask : getSubTask()) {
                fileWriter.write(subTaskToString(subTask) + "\n");
            }
            fileWriter.write("\n"); // Добавить пустую строку
            fileWriter.write(historyToString(getHistoryManager())); // Добавить идентификаторы задач из истории просмотров

        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла");
            throw new RuntimeException(e);
        }

    }

    // Метод сохранения Задачи в строку
    public String taskToString(Task task) {
        return task.getId() + ","
                + TaskType.TASK + ","
                + task.getName() + ","
                + task.getStatus() + ","
                + task.getDescription();
    }
    // Метод сохранения Подзадачи в строку
    public String subTaskToString(SubTask subTask) {
        return subTask.getId() + ","
                + TaskType.SUBTASK + ","
                + subTask.getName() + ","
                + subTask.getStatus() + ","
                + subTask.getDescription() + ","
                + subTask.getEpicId();
    }
    // Метод сохранения Эпика в строку
    public String epicToString(Epic epic) {
        return epic.getId() + ","
                + TaskType.EPIC + ","
                + epic.getName() + ","
                + epic.getStatus() + ","
                + epic.getDescription();
    }


    // Метод создания задачи из строки
//    public Task fromString(String value) {
//
//    }

    // Метод сохранения менеджера в историю в файл CSV
    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder str = new StringBuilder();

        if (history.isEmpty()) {
            return "";
        }

        for (Task task : history) {
            str.append(task.getId()).append(",");
        }

        if (str.length() != 0) {
            str.deleteCharAt(str.length() - 1);
        }

        return str.toString();
    }

    // Метод восстановления менеджера из истории из файла CSV
    /*static List<Integer> historyFromString(String value) {
        Files.readString(Path.of(path)); // Способ чтения файла
    }*/

    // Метод восстанавливает данные менеджера из файла при запуске программы
  /*  static FileBackedTasksManager loadFromFile(File file) {

    }*/

    /*
     * Теперь достаточно переопределить каждую модифицирующую операцию таким образом,
     * чтобы сначала выполнялась версия,
     * унаследованная от предка,
     * а затем — метод save
     */
    @Override
    public int addTask(Task task) {
        super.addTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
        return subTask.getId();
    }



    public static void main(String[] args) {
        TaskManager taskManager = new FileBackedTasksManager();

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

        System.out.println(taskManager.getTasks());
    }


}
