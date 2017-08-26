package lab8;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V extends Comparable> implements Map61B<K, V> {
    private int size;
    private Node root;

    private class Node<K, V> {
        private K key;
        private V value;
        private Node left;
        private Node right;

        private Node(K key, V value, Node left, Node right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }

    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKeyTraversal(key, root);
    }

    private boolean containsKeyTraversal(K key, Node current) {
        if (current == null) {
            return false;
        }

        int cmp = key.compareTo(current.key);
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return containsKeyTraversal(key, current.left);
        } else {
            return containsKeyTraversal(key, current.right);
        }
    }


    @Override
    public V get(K key) {
        return getTraversal(key, root);
    }

    private V getTraversal(K key, Node current) {
        if (current == null) {
            return null;
        }

        int cmp = key.compareTo(current.key);
        if (cmp == 0) {
            return (V) current.value;
        } else if (cmp < 0) {
            return getTraversal(key, current.left);
        } else {
            return getTraversal(key, current.right);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        root = putTraversal(key, value, root);
        size++;
    }

    public Node putTraversal(K key, V value, Node current) {
        if (current == null) {
            return new Node(key, value, null, null);
        }

        int cmp = key.compareTo(current.key);
        if (cmp == 0) {
            current.value = value;
        } else if (cmp < 0) {
            current.left = putTraversal(key, value, current.left);
        } else {
            current.right = putTraversal(key, value, current.right);
        }

        return current;
    }

    @Override
    public Set<K> keySet() {
        Set<K> kSet = new HashSet<>();
        keySetTraversal(kSet, root);
        return kSet;
    }

    private void keySetTraversal(Set<K> set, Node current) {
        if (current == null) {
            return;
        }

        keySetTraversal(set, current.left);
        set.add((K) current.key);
        keySetTraversal(set, current.right);
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new TreeIterator();
    }

    private class TreeIterator implements Iterator<K> {
        private K[] keys;
        private int index = 0;

        private TreeIterator() {
            keys = (K[]) new Object[size];
        }
        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public K next() {
            return keys[index++];
        }
    }

    public void printInOrder() {
        printTraversal(root);
    }

    private void printTraversal(Node current) {
        if (current == null) {
            return;
        }

        printTraversal(current.left);
        System.out.println("Key: " + current.key + " Value: " + current.value);
        printTraversal(current.right);
    }
}
