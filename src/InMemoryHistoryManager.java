import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> tasksHistory = new ArrayList<>();
    private static final int MAX_LIST_SIZE = 10; // Количество записей в списке истории

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (tasksHistory.size() > MAX_LIST_SIZE) {
            tasksHistory.remove(0);
        }
        tasksHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory;
    }
}