package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> tasksHistory = new HashMap<>();
    private final CustomLinkedList<Task> customHistoryList = new CustomLinkedList<>();


    @Override
    public void add(Task task) {
        if (task == null) { // Проверка
            return;
        }
        remove(task.getId());
        customHistoryList.linkLast(task);
        tasksHistory.put(task.getId(), customHistoryList.tail.prev);
    }

    @Override
    public void remove(int id) {
        if (tasksHistory.containsKey(id)) {
            customHistoryList.removeNode(tasksHistory.get(id));
            tasksHistory.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return customHistoryList.getTasks();
    }

    public static class CustomLinkedList<T> {
        private Node<T> head; // Указатель на первый элемент списка. Он же first
        private Node<T> tail; // Указатель на последний элемент списка. Он же last

        public void linkLast(T element) {
            if (head == null) {

                Node<T> currentNode = new Node<>(tail, element, null);
                head = currentNode;
                tail = new Node<>(currentNode, null, null);
                return;
            }
            Node<T> currentNode = tail;
            currentNode.data = element;
            tail = new Node<>(currentNode, null, null);
            currentNode.prev.next = currentNode;
            currentNode.next = tail;
        }

        public List<T> getTasks() {
            List<T> tasksList = new ArrayList<>();
            Node<T> node = head;

            while (node != null) {
                tasksList.add(node.data);
                node = node.next;
            }
            return tasksList;
        }

        public void removeNode(Node<T> node) {
            if (node == null) { // Проверка
                return;
            }

            final Node<T> prev = node.prev; // Ссылка на предыдущую ноду
            final Node<T> next = node.next; // Ссылка на следующую ноду

            if (prev == null) { // IF предыдущий узел == null, то головой списка становится NEXT узел
                head = next;
            } else { // IF узел находится в центре списка, тогда:
                prev.next = next; // то поле NEXT у предыдущей ноды, начинает ссылаться на поле NEXT удаляемой
                node.prev = null; // поле PREV у удаляемой ноды обнуляем, т.к. мы изменили ссылки и сохранили связь
            }
            if (next == null) { // IF следующий узел == null, то хвостом списка становится PREV узел
                tail = prev;
            } else { // IF узел находится в центре списка, тогда:
                next.prev = prev; // то поле PREV у следующей ноды, начинает ссылаться на поле PREV удаляемой
                node.next = null; // поле NEXT у удаляемой ноды обнуляем, т.к. мы изменили ссылки и сохранили связь
            }
            node.data = null; // Обнуляем значение узла
        }

    }
    @Override
    public String toString() {
        return "InMemoryHistoryManager{" + "tasksHistory=" + tasksHistory + '}';
    }
}



