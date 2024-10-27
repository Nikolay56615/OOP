package ru.nsu.lebedev.hashtable;

import java.util.Iterator;
import java.util.Objects;
import java.util.ConcurrentModificationException;

/**
 * Class hashtable with methods support a full set of operations.
 *
 * @param <K> type of key.
 * @param <V> type of value.
 */
public class HashTable<K, V> implements Iterable<Dict<K, V>> {
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity;
    private int size;
    private int modCount;
    private K[] keys;
    private V[] values;

    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
        this.modCount = 0;
        this.keys = (K[]) new Object[DEFAULT_CAPACITY];
        this.values = (V[]) new Object[DEFAULT_CAPACITY];
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    void clear() {
        size = 0;
        capacity = DEFAULT_CAPACITY;
        modCount = 0;
        keys = (K[]) new Object[DEFAULT_CAPACITY];
        values = (V[]) new Object[DEFAULT_CAPACITY];
    }

    private void resize() {
        int newCapacity = capacity * 2;
        K[] oldKeys = keys;
        V[] oldValues = values;
        keys = (K[]) new Object[newCapacity];
        values = (V[]) new Object[newCapacity];
        capacity = newCapacity;
        size = 0;
        for (int i = 0; i < oldKeys.length; i++) {
            if (oldKeys[i] != null) {
                put(oldKeys[i], oldValues[i]);
            }
        }
    }

    public void put(K key, V value) {
        if (size * 2 >= capacity) {
            resize();
        }
        int index = hash(key);
        while (keys[index] != null) {
            if (Objects.equals(keys[index], key)) {
                values[index] = value;
                return;
            }
            index = (index + 1) % capacity;
        }
        keys[index] = key;
        values[index] = value;
        size++;
        modCount++;
    }

    public V get(K key) {
        int index = hash(key);
        while (keys[index] != null) {
            if (key.equals(keys[index])) {
                return values[index];
            }
            index = (index + 1) % capacity;
        }
        return null;
    }

    public V remove(K key) {
        int index = hash(key);
        while (keys[index] != null) {
            if (Objects.equals(keys[index], key)) {
                V oldValue = values[index];
                values[index] = null;
                keys[index] = null;
                size--;
                modCount++;
                index = (index + 1) % capacity;
                while (keys[index] != null) {
                    K tempKey = keys[index];
                    V tempValue = values[index];
                    keys[index] = null;
                    values[index] = null;
                    size--;
                    put(tempKey, tempValue);
                    index = (index + 1) % capacity;
                }
                return oldValue;
            }
            index = (index + 1) % capacity;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public void update(K key, V value) {
        if (containsKey(key)) {
            put(key, value);
        }
    }

    public boolean equals(HashTable<K, V> other) {
        if (size != other.size) {
            return false;
        }
        for (int i = 0; i < capacity; i++) {
            if (keys[i] != null) {
                V value = other.get(keys[i]);
                if (Objects.equals(values[i], value)) {
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Dict<K, V> dict : this) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(dict.key).append(" = ").append(dict.value);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public Iterator<Dict<K, V>> iterator() {
        return new HashTableIterator();
    }

    private class HashTableIterator implements Iterator<Dict<K, V>> {
        private int currentIndex = 0;
        private int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            while (currentIndex < capacity && keys[currentIndex] == null) {
                currentIndex++;
            }
            return currentIndex < capacity;
        }

        @Override
        public Dict<K, V> next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            Dict<K, V> entry = new Dict<>(keys[currentIndex], values[currentIndex]);
            currentIndex++;
            return entry;
        }
    }
}
