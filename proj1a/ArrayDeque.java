public class ArrayDeque<Item> {

    private Item[] items;
    private int front;
    private int back;
    private int size;
    
    public ArrayDeque() {
        this.items = (Item []) new Object[8];
        this.size = 0;
        this.front = 4;
        this.back = 5;
    }

    private void resize(double factor) {
        Item[] newArray = (Item []) new Object [(int) (items.length * factor)];
        int numberOfItemsAfterFront = size - front; // Includes front
        int numberOfItemsBeforeFront = size - numberOfItemsAfterFront;

        System.arraycopy(items, front, newArray, 0, numberOfItemsAfterFront);
        System.arraycopy(items, 0, newArray, numberOfItemsAfterFront, size - numberOfItemsBeforeFront);

        items = newArray;

    }

    private int minusOne(int index) {
        if (index - 1 < 0) {
            return items.length - 1;
        }

        return index - 1;
    }

    private int plusOne(int index) {
        if (index + 1 >= items.length) {
            return 0;
        }

        return index + 1;
    }

    public void addFirst(Item newItem) {
        if (minusOne(front) == back) {
            resize(10);
        }

        front = minusOne(front);

        items[front] = newItem;
    }

    public void addLast(Item newItem) {
        if (plusOne(back) == front) {
            resize(10);
        }

        back = plusOne(front);

        items[back] = newItem;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            Item currentItem = get(i);
            System.out.println(currentItem);
        }
    }

    public Item removeFirst() {
        if (size == 0) {
            return null;
        }

        Item firstItem = items[front];
        items[front] = null;
        front = plusOne(front);

        if (items.length >= 16 && size / items.length < 0.25 ) {
            resize(0.5);
        }

        return firstItem;
    }

    public Item removeLast() {
        if (size == 0) {
            return null;
        }

        Item lastItem = items[back];
        items[back] = null;
        back = minusOne(back);

        if (items.length >= 16 && size / items.length < 0.25 ) {
            resize(0.5);
        }

        return lastItem;
    }

    public Item get(int index) {
        int arrayIndex = front + index;

        if (arrayIndex > items.length) {
            arrayIndex -= items.length;
        }
        
        return items[arrayIndex];
    }
}
