package manager;

import tasks.Task;

import java.util.List;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> tasksHistory = new LinkedList<>();
    private static final int MAX_LIST_SIZE = 10; // Количество записей в списке истории

    @Override
    public void add(Task task) {
        tasksHistory.add(task);
        if (tasksHistory.size() > MAX_LIST_SIZE) {
            tasksHistory.removeFirst();
        }
    }

    @Override
    public void remove(int id) {
        tasksHistory.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory;
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" + "tasksHistory=" + tasksHistory + '}';
    }

    public class CustomLinkedList<T> {
        private Node<T> head; // Указатель на первый элемент списка. Он же first
        private Node<T> tail; // Указатель на последний элемент списка. Он же last
        private int size = 0; // Длинна списка

        public void linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(tail, element, null);
            tail = newNode;
            if (oldTail == null)
                tail = newNode;
            else
                oldTail.next = newNode;
            size++;
        }

        public List<T> getTasks() {

        }

        public void removeNode(Node<T> node) {

        }

    }


}


