package manager;

import constants.TaskStatus;
import constants.Types;
import exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    Path path = Path.of("src/upload/tasks_file.csv");
    File file = new File(String.valueOf(path));
    public static final String COMMA_SEPARATOR = ",";

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
            Epic epic = new Epic(params[4], params[2], TaskStatus.valueOf(params[3].toUpperCase()));
            epic.setId(Integer.parseInt(params[0]));
            epic.setStatus(TaskStatus.valueOf(params[3].toUpperCase()));
            return epic;
        } else if ("SUBTASK".equals(params[1])) {
            SubTask subTask = new SubTask(params[4], params[2], TaskStatus.valueOf(params[3].toUpperCase()),
                    Integer.parseInt(params[5]));
            subTask.setId(Integer.parseInt(params[0]));
            return subTask;
        } else {
            Task task = new Task(Types.TASK, params[4], params[2], TaskStatus.valueOf(params[3].toUpperCase()), LocalDateTime.now(), 0L);
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
    public  FileBackedTasksManager loadFromFile(File file) {
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
        return fileBackedTasksManager;
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

    public static void main(String[] args) {

        FileBackedTasksManager manager = Managers.getDefaultFileManager();

        System.out.println("\nСоздание простой задачи");
        Task task16 = new Task(Types.TASK, "16 Наименование простой задачи 1", "1 Описание простой задачи 1", TaskStatus.NEW, LocalDateTime.now(), 0L);
        int task16Id = manager.addTask(task16);
        System.out.println("id простой задачи номер 1 task1Id: " + task16Id);
        Task task2 = new Task(Types.TASK, "2 Наименование простой задачи 2", "2 Описание простой задачи 2", TaskStatus.NEW, LocalDateTime.now(), 0L);
        int task2Id = manager.addTask(task2);
        System.out.println("id простой задачи номер 2 task2Id: " + task2Id);
        Task task3 = new Task(Types.TASK, "3 Наименование простой задачи 3", "3 Описание простой задачи 3", TaskStatus.NEW, LocalDateTime.now(), 0L);
        int task3Id = manager.addTask(task3);
        System.out.println("id простой задачи номер 3 task3Id: " + task3Id);

        System.out.println("\nСоздание Эпика 1 с 3-мя Подзадачами");
        Epic epic1 = new Epic("1 Наименование Эпик 1", "1 Описание Эпик 1", TaskStatus.NEW);
        int epic1Id = manager.addEpic(epic1);
        System.out.println("id Эпика номер 1 epic1Id: " + epic1Id);

        System.out.println("Создание Подзадачи 1 Эпика 1");
        SubTask subTask1 = new SubTask("1 Наименование Подзадачи 1", "1 Описание Подзадачи 1", TaskStatus.NEW, epic1Id);
        int subTask1Id = manager.addSubTask(subTask1);
        System.out.println("id Подзадачи номер 1 subTask1Id: " + subTask1Id);

        System.out.println("Создание Подзадачи 2 Эпика 1");
        SubTask subTask2 = new SubTask("2 Наименование Подзадачи 2", "2 Описание Подзадачи 2", TaskStatus.NEW, epic1Id);
        int subTask2Id = manager.addSubTask(subTask2);
        System.out.println("id Подзадачи номер 2 subTask2Id: " + subTask2Id);

        System.out.println("Создание Подзадачи 3 Эпика 1");
        SubTask subTask3 = new SubTask("3 Наименование Подзадачи 3", "3 Описание Подзадачи 3", TaskStatus.NEW, epic1Id);
        int subTask3Id = manager.addSubTask(subTask3);
        System.out.println("id Подзадачи номер 3 subTask3Id: " + subTask3Id);

        System.out.println("\nСоздание Эпика 2 без Подзадачи");
        Epic epic2 = new Epic("2 Наименование Эпик 2", "2 Описание Эпик 2", TaskStatus.NEW);
        int epic2Id = manager.addEpic(epic2);
        System.out.println("id Эпика номер 2 epic2Id: " + epic2Id);

        /*
        System.out.println("Создание Подзадачи 3 для 2 Эпика");
        SubTask subTask3 = new SubTask("Наименование Подзадачи 3", "Описание Подзадачи 3", Status.NEW, epic2Id);
        int subTask3Id = taskManager.addSubTask(subTask3);
        System.out.println("id Подзадачи номер 3 subTask3Id: " + subTask3Id);
        */

        System.out.println("\nВывод списков Эпиков, Задач, Подзадач");
        System.out.println(manager.getEpics());
        //     System.out.println(taskManager.getTasks());
        System.out.println(manager.getSubTask());

        System.out.println("\nОбновление простой задачи");
        Task taskUpdate = new Task(Types.TASK, "Обновили Наименования Задачи 1", "Обновили Описание Задачи 1", TaskStatus.IN_PROGRESS, LocalDateTime.now(), 0L);
        taskUpdate.setId(16);
        manager.updateTask(taskUpdate);
        manager.updateTask(new Task(Types.TASK, "Обновили Наименования Задачи 2", "Обновили Описание Задачи 2", TaskStatus.DONE, LocalDateTime.now(), 0L));

        System.out.println("\nОбновление подзадачи");
        manager.updateSubTask(new SubTask(
                "Обновили Наименование Подзадачи 1 Эпик 1", "Обновили Описание Подзадачи 1",
                TaskStatus.IN_PROGRESS, epic1Id));
        manager.updateSubTask(new SubTask(
                "Обновили Наименование Подзадачи 2 Эпик 1", "Обновили Описание Подзадачи 1",
                TaskStatus.DONE, epic1Id));
        manager.updateSubTask(new SubTask(
                "Обновили Наименование Подзадачи 1 Эпик 2", "Обновили Описание Подзадачи 1",
                TaskStatus.DONE, epic2Id));

        System.out.println("\nВызов методов");
        System.out.println(manager.getTaskById(task2Id));
        System.out.println("История просмотров16: " + manager.getHistory());
        System.out.println(manager.getTaskById(task2Id));
        System.out.println("История просмотров2: " + manager.getHistory());
        System.out.println(manager.getTaskById(task16Id));
        System.out.println("История просмотров3: " + manager.getHistory());
        System.out.println(manager.getTaskById(task3Id));
        System.out.println("История просмотров4: " + manager.getHistory());
        manager.getTaskById(task3Id);
        manager.getTaskById(task3Id);
        manager.getTaskById(task3Id);
        manager.getTaskById(task2Id);
        manager.getTaskById(task2Id);
        manager.getTaskById(task3Id);
        manager.getTaskById(task3Id);
        manager.getTaskById(task16Id);
        manager.getTaskById(task16Id);
        System.out.println("\nИстория просмотров несколько: " + manager.getHistory());

        System.out.println("\nВызов Эпиков");
        System.out.println(manager.getEpicById(epic1Id));
        System.out.println("История просмотров: " + manager.getHistory());
        System.out.println(manager.getEpicById(epic2Id));
        System.out.println("История просмотров: " + manager.getHistory());
        manager.getEpicById(epic1Id);
        manager.getEpicById(epic1Id);
        manager.getEpicById(epic2Id);
        manager.getEpicById(epic2Id);
        manager.getEpicById(epic2Id);
        manager.getSubTaskById(subTask1Id);
        manager.getSubTaskById(subTask2Id);
        manager.getSubTaskById(subTask2Id);
        System.out.println("История просмотров: " + manager.getHistory());

        System.out.println(manager.getSubTaskById(subTask1Id));
        System.out.println("\nИстория просмотров: " + manager.getHistory());
        System.out.println(manager.getSubTaskById(subTask3Id));
        System.out.println("История просмотров: " + manager.getHistory());
        System.out.println(manager.getSubTaskById(subTask2Id));
        System.out.println("История просмотров: " + manager.getHistory());
        System.out.println(manager.getSubTaskById(subTask3Id));
        System.out.println("История просмотров: " + manager.getHistory());

        System.out.println(manager.getTaskById(task16Id));
        System.out.println("\nИстория просмотров: " + manager.getHistory());
        System.out.println(manager.getTaskById(task3Id));
        System.out.println("История просмотров: " + manager.getHistory());
        System.out.println(manager.getTaskById(task2Id));
        System.out.println("История просмотров: " + manager.getHistory());
        System.out.println(manager.getTaskById(task16Id));
        System.out.println("История просмотров: " + manager.getHistory());

        manager.deleteByIdTask(task16Id);
        manager.deleteByIdTask(task2Id);
        manager.deleteByIdTask(task3Id);
        System.out.println("\nУдалённая задача 1: " + manager.getTaskById(task16Id));
        System.out.println("История просмотров: " + manager.getHistory());

     /*   FileBackedTasksManager managerFile = FileBackedTasksManager.loadFromFile(new File("src/upload/tasks_file.csv"));

        for (Task task : managerFile.getTasks()) {
            boolean res = task.toString().equals(managerFile.getTasks().get(task.getId()).toString());
            if (!res) {
                System.out.println("Не сходятся объекты из файла");
            }
        }*/
    }

}
