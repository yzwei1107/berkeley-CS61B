public class LinkedListDeque<Item> {

    private Node sentinel;
    private int size;
    
    public LinkedListDeque() {
        this.sentinel = new Node(null, sentinel, sentinel);
        sentinel.next = sentinel;
        sentinel.previous = sentinel;
        this.size = 0;
    }

    public void addFirst(Item newItem) {
        Node oldFrontNode = sentinel.next;
        sentinel.next = new Node(newItem, sentinel, sentinel.next);
        oldFrontNode.previous = sentinel.next;
        size++;
    }

    public void addLast(Item newItem) {
        Node oldBackNode = sentinel.previous;
        sentinel.previous = new Node (newItem, sentinel.previous, sentinel);
        oldBackNode.next = sentinel.previous;
        size++;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }

        return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node p = sentinel;

        while (p.next != sentinel) {
        System.out.print(p.next.item + " ");
        p = p.next;
        }
    }

    public Item removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }

        Item oldFrontItem = sentinel.next.item;

        sentinel.next = sentinel.next.next;       
        sentinel.next.previous = sentinel;
        size--;

        return oldFrontItem;
    }

    public Item removeLast() {
        if (sentinel.previous == sentinel) {
            return null;
        }

        Item oldBackItem = sentinel.previous.item;

        sentinel.previous = sentinel.previous.previous;
        sentinel.previous.next = sentinel;
        size--;

        return oldBackItem;
    }

    public Item get(int index) {
        if (index > size - 1) {
            return null;
        }

        int lastIndex = size - 1;

        if (index > lastIndex / 2) {
            return getFromBack(index);
        } else {
            return getFromFront(index);
        }
    }

    private Item getFromFront(int index) {
        int currentIndex = 0;
        Node p = sentinel.next;

        while  (currentIndex != index) {
            p = p.next;
            currentIndex++;
        }

        return p.item;
    }

    private Item getFromBack(int index) {
        int currentIndex = size - 1;
        Node p = sentinel.previous;

        while  (currentIndex != index) {
            p = p.next;
            currentIndex--;
        }

        return p.item;

    }

    public Item getRecursive(int index) {
        if (index > size - 1) {
            return null;
        }

        int lastIndex = size - 1;
 
        if (index > lastIndex / 2) {
            Node p = sentinel.previous;
            return getFromBackRecursive(p, index);
        } else {
            Node p = sentinel.next;
            return getFromFrontRecursive(p, lastIndex - index);
        }
    }

    private Item getFromFrontRecursive(Node currentNode, int currentIndex) {
        if (currentIndex == 0) {
            return currentNode.item;
        }

        return getFromFrontRecursive(currentNode.next, --currentIndex);

    }

    private Item getFromBackRecursive(Node currentNode, int currentIndex) {
        if (currentIndex == 0) {
            return currentNode.item;
        }

        return getFromBackRecursive(currentNode.previous, --currentIndex);
    }

    public class Node {
        public Item item;
        public Node previous;
        public Node next;

        public Node(Item i, Node p, Node n) {
            this.item = i;
            this.previous = p;
            this.next = n;
        }

    }
}
