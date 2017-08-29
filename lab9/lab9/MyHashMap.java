package lab9;

import java.util.*;

/**
 * Hash-based implementation of map using separate chaining for collisions
 * @author moboa
 * @param <K> Key
 * @param <V> Value
 */

public class MyHashMap<K, V> implements Map61B<K, V> {
    private static final int DEFAULT_INITIAL_SIZE = 8;
    private static final double DEFAULT_LOAD_FACTOR = 1;

    private LinkedList<Node<K, V>>[] table;
    private int size = 0;
    private double idealLoadFactor;

    private class Node<K, V> {
        private K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        this.idealLoadFactor = DEFAULT_LOAD_FACTOR;
        this.table = (LinkedList<Node<K, V>>[]) new LinkedList[DEFAULT_INITIAL_SIZE];
    }

    public MyHashMap(int initialSize) {
        this.idealLoadFactor = DEFAULT_LOAD_FACTOR;
        this.table = (LinkedList<Node<K, V>>[]) new LinkedList[initialSize];
    }

    public MyHashMap(int initialSize, double loadFactor) {
        this.idealLoadFactor = loadFactor;
        this.table = (LinkedList<Node<K,V>>[]) new LinkedList[initialSize];
    }

    /* Returns the index of the table that corresponds to the key */
    private int hash(K key) {
        return Math.floorMod(key.hashCode(), table.length);
    }

    @Override
    public void clear() {
        table = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        if (table == null) {
            return false;
        }
        int index = hash(key);
        if (table[index] == null) {
            return false;
        }

        for (Node node : table[index]) {
            if (node.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        }

        int index = hash(key);
        for (Node node : table[index]) {
            if (node.key.equals(key)) {
                return (V) node.value;
            }
        }

        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            table = (LinkedList<Node<K, V>>[]) new LinkedList[DEFAULT_INITIAL_SIZE];
        } else if (size / table.length > idealLoadFactor) {
            resize(2 * table.length);
        }

        int index = hash(key);
        if (table[index] == null) {
            table[index] = (LinkedList<Node<K, V>>) new LinkedList();
        } else if (containsKey(key)) {
            for (Node node : table[index]) {
                if (node.key.equals(key)) {
                    node.value = value;
                    return;
                }
            }
        }

        table[index].add(new Node(key, value));
        size++;
    }

    private void resize(int newCapacity) {
        LinkedList<Node <K, V>>[] newArray = (LinkedList<Node <K, V>>[]) new LinkedList[newCapacity];
        Set<K> keys = keySet();

        for (K key : keys) {
            int index = hash(key);

            if (newArray[index] == null) {
                newArray[index] = new LinkedList<>();
            }
            newArray[index].add(new Node<>(key, get(key)));
        }

        table = newArray;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<K>();

        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                continue;
            }

            for (int j = 0; j < table[i].size(); j++) {
                Node node = (Node) table[i].get(j);
                keys.add((K) node.key);
            }
        }
        return keys;
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
        return new MapIterator();
    }

    private class MapIterator implements Iterator<K> {
        private ArrayList<K> keys;
        private int index = 0;

        private MapIterator() {
            keys = new ArrayList<>(keySet());
        }

        @Override
        public boolean hasNext() {
            return index < keys.size();
        }

        @Override
        public K next() {
            return keys.get(index++);
        }
    }
}
