package synthesizer;// TODO: Make sure to make this class a part of the synthesizer package
// package <package name>;
import synthesizer.AbstractBoundedQueue;

import java.util.Iterator;

/**
 * Array-based implementation of the ring buffer data structure
 * @author moboa
 */
public class ArrayRingBuffer<Item> extends AbstractBoundedQueue<Item> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private Item[] items;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.capacity = capacity;
        this.items = (Item[]) new Object[capacity];
        this.fillCount = 0;
        this.first = 0;
        this.last = 0;

    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(Item x) {
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }

        items[last] = x;
        last = plusOne(last);
        fillCount++;
    }


    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public Item dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }

        Item oldestItem = items[first];
        items[first] = null;
        first = plusOne(first);
        fillCount--;

        return oldestItem;
    }

    /* Returns the next index in the ring buffer. */
    private int plusOne(int index) {
        if (index + 1 == capacity) {
            return 0;
        }

        return index + 1;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public Item peek() {
        return items[first];
    }

    // TODO: When you get to part 5, implement the needed code to support iteration.
}
