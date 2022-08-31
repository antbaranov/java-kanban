package manager;

public class Node<E> {
    public E data;
    public Node<E> prev;
    public Node<E> next;

    public Node(Node<E> prev, E data, Node<E> next) {
        this.prev = prev;
        this.data = data;
        this.next = next;
    }
}
