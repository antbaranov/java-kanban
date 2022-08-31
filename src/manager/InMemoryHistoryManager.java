package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" + "tasksHistory=" + tasksHistory + '}';
    }

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

    public class CustomLinkedList<Task> {
        private Node<Task> head; // Указатель на первый элемент списка. Он же first
        private Node<Task> tail; // Указатель на последний элемент списка. Он же last

        public void linkLast(Task task) {
            if (head == null) {

                Node<Task> currentNode = new Node<>(tail, task, null);
                head = currentNode;
                tail = new Node<>(currentNode, null, null);
                return;
            }
            Node<Task> currentNode = tail;
            currentNode.data = task;
            tail = new Node<>(currentNode, null, null);
            currentNode.prev.next = currentNode;
            currentNode.next = tail;
        }

        public List<Task> getTasks() {
            List<Task> tasksList = new ArrayList<>();
            Node<Task> node = head;

            while (node != null) {
                tasksList.add(node.data);
                node = node.next;
            }
            return tasksList;
        }

        public void removeNode(Node<Task> node) {
            if (node == null) { // <---------------- *** Нужно ли делать проверку? *** -------------
                return;
            }

            final Node<Task> prev = node.prev; // Ссылка на предыдущую ноду
            final Node<Task> next = node.next; // Ссылка на следующую ноду

            if (prev == null) { // IF предыдущий узел == null, ELSE головой списка становится NEXT узел
                head = next;
            } else { // IF узел находится в центре списка, ELSE:
                prev.next = next; // ELSE поле NEXT у предыдущей ноды, начинает ссылаться на поле NEXT удаляемой
                node.prev = null; // поле PREV у удаляемой ноды обнуляем, т.к. мы изменили ссылки и сохранили связь
            }
            if (next == null) { // IF следующий узел == null, то хвостом списка становится PREV узел
                tail = prev;
            } else { // IF узел находится в центре списка, ELSE:
                next.prev = prev; // ELSE поле PREV у следующей ноды, начинает ссылаться на поле PREV удаляемой
                node.next = null; // поле NEXT у удаляемой ноды обнуляем, т.к. мы изменили ссылки и сохранили связь
            }
            node.data = null; // Обнуляем значение узла
        }

    }
}



