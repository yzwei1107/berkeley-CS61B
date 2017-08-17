public class LinkedListDeque<Item> {
    private Node sentinel;
    private int size;

    /* A node stores an item, a pointer to the previous node in the deque and a pointer
       to the next node in the deque. */
    private static class Node<Item> {
        Item item;
        Node previous, next;

        Node(Item item, Node previous, Node next) {
            this.item = item;
            this.previous = previous;
            this.next = next;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.previous = sentinel;
        size = 0;
    }

    /* Adds an item to the front of the Deque. */
    public void addFirst(Item newItem) {
        Node newFirst = new Node(newItem, sentinel, sentinel.next);
        sentinel.next.previous = newFirst;
        sentinel.next = newFirst;
        size++;
    }

    /* Adds an item to the back of the Deque. */
    public void addLast(Item newItem) {
        Node newLast = new Node(newItem, sentinel.previous, sentinel);
        sentinel.previous.next = newLast;
        sentinel.previous = newLast;
        size++;
    }

    /* Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    /* Returns number of items in the deque. */
    public int size() {
        return size;
    }

    /* Prints the items in the Deque from first to last, separated by a space. */
    public void printDeque() {
        Node pointer = sentinel.next;

        while (pointer != sentinel) {
            System.out.print(pointer.item + " ");
            pointer = pointer.next;
        }

    }

    /* Removes and returns the item at the front of the Deque. If no such item exists,
       returns null. */
    public Item removeFirst() {
        if (size == 0) {
            return null;
        }

        Item firstItem = (Item) sentinel.next.item;
        Node newFirst = sentinel.next.next;
        newFirst.previous = sentinel;
        sentinel.next = newFirst;
        size--;

        return firstItem;
    }

    /* Removes and returns the item at the back of the Deque. If no such item exists,
       returns null. */
    public Item removeLast() {
        if (size == 0) {
            return null;
        }

        Item lastItem = (Item) sentinel.previous.item;
        Node newLast = sentinel.previous.previous;
        newLast.next = sentinel;
        sentinel.previous = newLast;
        size--;

        return lastItem;
    }

    /* Gets the item at the given index using iteration. If no such item exists, returns null. */
    public Item get(int index) {
        if (index >= size || index < 0) {
            return null;
        }

        Node pointer = sentinel.next;
        int i = 0;

        while (i < index) {
            pointer = pointer.next;
            i++;
        }

        return (Item) pointer.item;
    }

    /* Gets the item at the given index using recursion. If no such item exists, returns null. */
    public Item getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }

        return getRecursiveHelper(sentinel.next, index);
    }

    /* Helper method for getRecursive */
    private Item getRecursiveHelper(Node current, int index) {

        if (index == 0) {
            return (Item) current.item;
        }

        return getRecursiveHelper(current.next, index - 1);
    }
}
