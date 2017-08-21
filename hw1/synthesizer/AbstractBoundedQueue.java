package synthesizer;

/**
 * @author moboa
 */
public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    /* The number of items in the queue. */
    protected int fillCount;

    /* The maximum number of items the queue can hold. */
    protected int capacity;

    /* Returns the maximum number of items the queue can hold. */
    @Override
    public int capacity() {
        return capacity;
    }

    /* Returns the number of items in the queue */
    @Override
    public int fillCount() {
        return fillCount;
    }
}
