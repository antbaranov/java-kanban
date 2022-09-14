package manager;

public class Node<E> {
    E data;
    public Node<E> prev;
     Node<E> next;

    Node(Node<E> prev, E data, Node<E> next) {
        this.prev = prev;
        this.data = data;
        this.next = next;
    }
}
