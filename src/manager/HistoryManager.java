package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();


      static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();

        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(",");
        }

        if (sb.isEmpty()) {
            sb.append(0);
        } else {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

}
