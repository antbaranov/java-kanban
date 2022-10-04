package manager;

import constants.Status;
import constants.Types;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    Path path = Path.of("src/upload/tasks_file.csv");
    File file = new File(String.valueOf(path));
    public static final String COMMA_SEPARATOR = ",";

    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);

    }
    public FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }


    // Метод сохраняет текущее состояние менеджера в указанный файл
    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,type,name,status,description,epic" + "\n"); // Запись шапки с заголовками в файл
            for (Task task : getAllTasks()) {
                bufferedWriter.write(taskToString(task) + "\n");
            }
            for (Epic epic : this.getAllEpics()) {
                bufferedWriter.write(epicToString(epic) + "\n");
            }
            for (SubTask subTask : getAllSubtasks()) {
                bufferedWriter.write(subTaskToString(subTask) + "\n");
            }
            bufferedWriter.write("\n"); // Добавить пустую строку
            bufferedWriter.write(HistoryManager.historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла");
        }
    }

    // Метод сохранения Задачи в строку
    public String taskToString(Task task) {
        return task.getId() + COMMA_SEPARATOR
                + Types.TASK + COMMA_SEPARATOR
                + task.getName() + COMMA_SEPARATOR
                + task.getStatus() + COMMA_SEPARATOR
                + task.getDescription();
    }

    // Метод сохранения Подзадачи в строку
    public String subTaskToString(SubTask subTask) {
        return subTask.getId() + COMMA_SEPARATOR
                + Types.SUBTASK + COMMA_SEPARATOR
                + subTask.getName() + COMMA_SEPARATOR
                + subTask.getStatus() + COMMA_SEPARATOR
                + subTask.getDescription() + COMMA_SEPARATOR
                + subTask.getEpicId();
    }

    // Метод сохранения Эпика в строку
    public String epicToString(Epic epic) {
        return epic.getId() + COMMA_SEPARATOR
                + Types.EPIC + COMMA_SEPARATOR
                + epic.getName() + COMMA_SEPARATOR
                + epic.getStatus() + COMMA_SEPARATOR
                + epic.getDescription();
    }


    // Метод создания задачи из строки
    public static Task fromString(String value) {
        String[] params = value.split(COMMA_SEPARATOR);
        if ("EPIC".equals(params[1])) {
            Epic epic = new Epic(params[4], params[2], Status.valueOf(params[3].toUpperCase()));
            epic.setId(Integer.parseInt(params[0]));
            epic.setStatus(Status.valueOf(params[3].toUpperCase()));
            return epic;
        } else if ("SUBTASK".equals(params[1])) {
            SubTask subTask = new SubTask(params[4], params[2], Status.valueOf(params[3].toUpperCase()),
                    Integer.parseInt(params[5]));
            subTask.setId(Integer.parseInt(params[0]));
            return subTask;
        } else {
            Task task = new Task(params[4], params[2], Status.valueOf(params[3].toUpperCase()));
            task.setId(Integer.parseInt(params[0]));
            return task;
        }
    }

    // Метод восстановления менеджера из истории из файла CSV
    static List<Integer> historyFromString(String value) {
        List<Integer> list = new ArrayList<>();
        if (value != null) {
            String[] val = value.split(COMMA_SEPARATOR);
            for (String number : val) {
                list.add(Integer.parseInt(number));
            }
            return list;
        }
        return list;
    }

    // Метод восстанавливает данные менеджера из файла при запуске программы
    public  void loadFromFile() {
       // final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();

                if (line.isEmpty() || line.isBlank()) {
                    break;
                }
                Task task = fromString(line);

                if (task instanceof Epic epic) {
                    addEpic(epic);
                } else if (task instanceof SubTask subTask) {
                    addSubTask(subTask);
                } else {
                    addTask(task);
                }
            }
            String lineWithHistory = br.readLine();
            for (int id : historyFromString(lineWithHistory)) {
                addToHistory(id);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла!");
        }
       // return fileBackedTasksManager;
    }

    /*
     * Теперь достаточно переопределить каждую модифицирующую операцию таким образом,
     * чтобы сначала выполнялась версия,
     * унаследованная от предка,
     * а затем — метод save
     */

    // Переопределение методов
    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
        return subTask;
    }

    // Удаление всех Задач
    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    // Удаление всех ПодЗадач
    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    // Удаление всех Эпиков
    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    // Получение списка Эпиков
    @Override
    public List<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    // Получение списка задач
    @Override
    public List<Task> getAllTasks() {

        return super.getAllTasks();
    }

    // Получение списка подзадач
    @Override
    public List<SubTask> getAllSubtasks() {
        return super.getAllSubtasks();

    }

    // Получение Задач по идентификатору
    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    // Получение Подзадач по идентификатору
    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    // Получение Эпика по идентификатору
    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    // Обновление Задач (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    // Обновление Подзадач (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    // Обновление Эпиков (Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра)
    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    // Обновление статуса Эпиков
    @Override
    public void updateStatusEpic(Epic epic) {
        super.updateStatusEpic(epic);
        save();
    }

    // Удаление Задачи по идентификатору
    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    // Удаление Подзадачи по идентификатору
    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    // Удаление Эпика по идентификатору
    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }



    public static void main(String[] args) {




    }

}
