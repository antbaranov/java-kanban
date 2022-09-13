package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    // Метод сохраняет текущее состояние менеджера в указанный файл
    public void save() {

    }

    // Метод сохранения задачи в строку
    public String toString(Task task) {

    }


    // Метод создания задачи из строки
    Task fromString(String value) {

    }

    // Метод сохранения менеджера в историю в файл CSV
    static String historyToString(HistoryManager manager) {

    }

    // Метод восстановления менеджера из истории из файла CSV
    static List<Integer> historyFromString(String value) {
        Files.readString(Path.of(path)); // Способ чтения файла
    }

    // Метод восстанавливает данные менеджера из файла при запуске программы
    static FileBackedTasksManager loadFromFile(File file) {

    }

    /*
     * Теперь достаточно переопределить каждую модифицирующую операцию таким образом,
     * чтобы сначала выполнялась версия,
     * унаследованная от предка,
     * а затем — метод save
     */
    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save(task);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save(epic);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save(subTask);
    }


    static void main(String[] args) {

    }


}
