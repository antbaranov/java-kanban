package manager;

import tasks.Task;
import java.util.List;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> tasksHistory = new LinkedList<>();
    private static final int MAX_LIST_SIZE = 10; // Количество записей в списке истории

    @Override
    public void add(Task task) {
        tasksHistory.add(task);
        if (tasksHistory.size() > MAX_LIST_SIZE) {
            tasksHistory.removeFirst();
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return tasksHistory;
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "tasksHistory=" + tasksHistory +
                '}';
    }
}


