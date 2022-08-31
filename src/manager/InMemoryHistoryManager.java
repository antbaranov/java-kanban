package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> tasksHistory = new HashMap<>();
    private final CustomLinkedList<Task> customList = new CustomLinkedList<>();


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        customList.linkLast(task);
        tasksHistory.put(task.getId(), customList.tail.prev);
    }

    @Override
    public void remove(int id) {

        customList.removeNode(tasksHistory.remove(id)); // Удаление узла
    }

    @Override
    public List<Task> getHistory() {
        return customList.getTasks();
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" + "tasksHistory=" + tasksHistory + '}';
    }

    public class CustomLinkedList<T> {
        private Node<T> head; // Указатель на первый элемент списка. Он же first
        private Node<T> tail; // Указатель на последний элемент списка. Он же last

        public void linkLast(T element) {
            if (head == null) {
                // T value, Node<T> prev, Node<T> next
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
            if (node == null) { // <---------------- *** Нужно ли делать проверку? *** -------------
                return;
            }

            final Node<T> prev = node.prev; // Ссылка на предыдущую ноду
            final Node<T> next = node.next; // Ссылка на следующую ноду

            if (prev == null) { // IF предыдущий узел == null, ELSE головой списка становится NEXT узел
                head = next;
            } else { // IF узел находится в центре списка, ELSE:
                prev.next = next; // ТО поле NEXT у предыдущей ноды, начинает ссылаться на поле NEXT удаляемой
                node.prev = null; // поле PREV у удаляемой ноды зануляем, т.к. мы изменили ссылки и сохранили связь
            }
            if (next == null) { // IF следующий узел == null, то хвостом списка становится PREV узел
                tail = prev;
            } else { // IF узел находится в центре списка, ТО:
                next.prev = prev; // ТО поле PREV у следующей ноды, начинает ссылаться на поле PREV удаляемой
                node.next = null; // поле NEXT у удаляемой ноды зануляем, т.к. мы изменили ссылки и сохранили связь
            }
            node.data = null; // Зануляем значение узла
        }
    }

}


