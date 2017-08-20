/**
 *  Interface for the abstract data type Deque
 *
 * @author moboa
 */
interface Deque<Item> {

    /* Adds an item to the front of the Deque. */
    void addFirst(Item i);

    /* Adds an item to the back of the Deque. */
    void addLast(Item i);

    /* Returns true if deque is empty, false otherwise. */
    boolean isEmpty();

    /* Returns number of items in the deque. */
    int size();

    /* Prints the items in the Deque from first to last, separated by a space. */
    void printDeque();

    /* Removes and returns the item at the front of the Deque. If no such item exists,
       returns null. */
    Item removeFirst();

    /* Removes and returns the item at the back of the Deque. If no such item exists,
       returns null. */
    Item removeLast();

    /* Gets the item at the given index. If no such item exists, returns null. */
    Item get(int index);
}
