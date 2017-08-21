package synthesizer;
// package <package name>;

import java.util.Iterator;

/**
 * Array-based implementation of the ring buffer data structure
 * @author moboa
 */
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] items;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.capacity = capacity;
        this.items = (T[]) new Object[capacity];
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
    public void enqueue(T x) {
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
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }

        T oldestT = items[first];
        items[first] = null;
        first = plusOne(first);
        fillCount--;

        return oldestT;
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
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return items[first];
    }

    @Override
    public Iterator<T> iterator() {
        return new BufferIterator();
    }

    private class BufferIterator implements Iterator<T> {
        private int position = first;
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < fillCount;
        }

        @Override
        public T next() {
            T currentItem = items[position];
            position = plusOne(position);
            index++;
            return currentItem;
        }
    }

}
