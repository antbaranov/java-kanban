package manager;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    Path path = Path.of("tasks_file.csv");
    File file = new File(String.valueOf(path));
    static final String COMMA_SEPARATOR = ",";
    public FileBackedTasksManager() {
    }

    // Метод сохраняет текущее состояние менеджера в указанный файл
    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,type,name,status,description,epic" + "\n"); // Запись шапки с заголовками в файл
            for (Task task : getTasks()) {
                bufferedWriter.write(taskToString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                bufferedWriter.write(epicToString(epic) + "\n");
            }
            for (SubTask subTask : getSubTask()) {
                bufferedWriter.write(subTaskToString(subTask) + "\n");
            }
            bufferedWriter.write("\n"); // Добавить пустую строку
            bufferedWriter.write(HistoryManager.historyToString(getHistoryManager())); // Добавить идентификаторы задач из истории просмотров  HistoryManager.historyToString(historyManager)

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла");
        }

    }

    // Метод сохранения Задачи в строку
    public String taskToString(Task task) {
        return task.getId() + COMMA_SEPARATOR
                + TaskType.TASK + COMMA_SEPARATOR
                + task.getName() + COMMA_SEPARATOR
                + task.getStatus() + COMMA_SEPARATOR
                + task.getDescription();
    }

    // Метод сохранения Подзадачи в строку
    public String subTaskToString(SubTask subTask) {
        return subTask.getId() + COMMA_SEPARATOR
                + TaskType.SUBTASK + COMMA_SEPARATOR
                + subTask.getName() + COMMA_SEPARATOR
                + subTask.getStatus() + COMMA_SEPARATOR
                + subTask.getDescription() + COMMA_SEPARATOR
                + subTask.getEpicId();
    }

    // Метод сохранения Эпика в строку
    public String epicToString(Epic epic) {
        return epic.getId() + COMMA_SEPARATOR
                + TaskType.EPIC + COMMA_SEPARATOR
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

    public  void loadFromFile(File file) {
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
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
    public int addTask(Task task) {
        super.addTask(task);
        save();
        return task.getId();
    }

    @Override
    public  int addEpic(Epic epic) {
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

    // Удаление всех Задач
    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    // Удаление всех ПодЗадач
    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    // Удаление всех Эпиков
    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    // Получение списка Эпиков
    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    // Получение списка задач
    @Override
    public List<Task> getTasks() {

        return super.getTasks();
    }

    // Получение списка подзадач
    @Override
    public List<SubTask> getSubTask() {
        return super.getSubTask();

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
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    // Удаление Задачи по идентификатору
    @Override
    public void deleteByIdTask(int id) {
        super.deleteByIdTask(id);
        save();
    }

    // Удаление Подзадачи по идентификатору
    @Override
    public void deleteByIdSubTask(int id) {
        super.deleteByIdSubTask(id);
        save();
    }

    // Удаление Эпика по идентификатору
    @Override
    public void deleteByIdEpic(int id) {
        super.deleteByIdEpic(id);
        save();
    }

    // Получение списка всех подзадач определённого эпика
    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(int id) {
        ArrayList<SubTask> subTasksNew = super.getSubTasksOfEpic(id);
        save();
        return subTasksNew;

    }

    // Метод собственное непроверяемое исключение
    static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(final String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        Path path = Path.of("tasks_file.csv");
        File file = new File(String.valueOf(path));
        // FileBackedTasksManager taskManager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);

        FileBackedTasksManager taskManager = Managers.getDefaultFileManager();


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
        taskManager.getTaskById(task2Id);
        taskManager.getTaskById(task3Id);
        taskManager.getTaskById(task3Id);
        taskManager.getTaskById(task1Id);
        taskManager.getEpicById(epic1Id);
        taskManager.getSubTaskById(subTask1Id);
        taskManager.getSubTaskById(subTask2Id);
/*
        System.out.println("Восстановили таски: " + managerFile.getTasks());
        System.out.println("Восстановили эпики: " + managerFile.getEpics());
        System.out.println("Восстановили субтаски: " + managerFile.getSubTask());
        System.out.println("Восстановили историю запросов: " + managerFile.getHistory());
*/
    }

}
