package ru.nsu.lebedev.hashtable;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Class hashtable with methods support a full set of operations.
 *
 * @param <K> type of key.
 * @param <V> type of value.
 */
public class HashTable<K, V> implements Iterable<HashTableEntry<K, V>> {
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity;
    private int size;
    private int modCount;
    private K[] keys;
    private V[] values;

    /**
     * Initial method for hashtable.
     */
    @SuppressWarnings("unchecked")
    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
        this.modCount = 0;
        this.keys = (K[]) new Object[DEFAULT_CAPACITY];
        this.values = (V[]) new Object[DEFAULT_CAPACITY];
    }

    /**
     * Method for creating hash key.
     *
     * @param key key for value.
     * @return hash of key or 0 if key doesn't have value.
     */
    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    /**
     * Method for clear hashtable.
     */
    @SuppressWarnings("unchecked")
    void clear() {
        size = 0;
        capacity = DEFAULT_CAPACITY;
        modCount = 0;
        keys = (K[]) new Object[DEFAULT_CAPACITY];
        values = (V[]) new Object[DEFAULT_CAPACITY];
    }

    /**
     * Method for resize hashtable if size * 2 bigger than capacity.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        final K[] oldKeys = keys;
        final V[] oldValues = values;
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

    /**
     * Method for resize hashtable if size * 2 bigger than capacity.
     *
     * @param key new key.
     * @param value new value.
     */
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

    /**
     * Method for getting value from hashtable[key].
     *
     * @param key key.
     * @return hashtable[key] or null if it doesn't exist.
     */
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

    /**
     * Method for remove key and value from hashtable.
     *
     * @param key key that will be deleted.
     * @return oldValue that was remove or null if it doesn't exist.
     */
    public V remove(K key) {
        int index = hash(key);
        while (keys[index] != null) {
            if (Objects.equals(keys[index], key)) {
                final V oldValue = values[index];
                values[index] = null;
                keys[index] = null;
                size--;
                modCount++;
                index = (index + 1) % capacity;
                while (keys[index] != null) {
                    final K tempKey = keys[index];
                    final V tempValue = values[index];
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

    /**
     * Method for getting size of hashtable.
     *
     * @return size.
     */
    public int size() {
        return size;
    }

    /**
     * Method for getting capacity of hashtable.
     *
     * @return capacity.
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Method for checking key existence in hashtable.
     *
     * @param key exist.
     * @return True or False.
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Method for updating the value by key.
     *
     * @param key old key.
     * @param value new value.
     */
    public void update(K key, V value) {
        if (containsKey(key)) {
            put(key, value);
        }
    }

    /**
     * Method for checking equality of two hashtable.
     *
     * @param o other hashtable.
     * @return True or False.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HashTable<K, V> other = (HashTable<K, V>) o;
        if (this.size != other.size || this.hashCodeAll() != other.hashCodeAll()) {
            return false;
        }
        for (int i = 0; i < capacity; i++) {
            if (keys[i] != null) {
                V value = other.get(keys[i]);
                if (!Objects.equals(values[i], value)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method for getting hash of all values of hashtable.
     *
     * @return hash of hashtable.
     */
    private int hashCodeAll() {
        int hash = 0;
        for (int i = 0; i < capacity; i++) {
            if (keys[i] != null) {
                hash += Objects.hashCode(keys[i]) ^ Objects.hashCode(values[i]);
            }
        }
        return hash;
    }

    /**
     * Method for getting string representation of the hash table.
     *
     * @return string representation.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (HashTableEntry<K, V> dict : this) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(dict.key).append(" = ").append(dict.value);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Iterator of hashtable.
     *
     * @return pointer to new iterator class of hashtable.
     */
    @Override
    public Iterator<HashTableEntry<K, V>> iterator() {
        return new HashTableIterator();
    }

    /**
     * Class realization of iterator for hashtable.
     */
    private class HashTableIterator implements Iterator<HashTableEntry<K, V>> {
        private int currentIndex = 0;
        private final int expectedModCount = modCount;

        /**
         * Method for checking existence of next element of hashtable.
         *
         * @return True or False.
         */
        @Override
        public boolean hasNext() {
            while (currentIndex < capacity && keys[currentIndex] == null) {
                currentIndex++;
            }
            return currentIndex < capacity;
        }

        /**
         * Method for getting next element of hashtable.
         *
         * @return True or False.
         */
        @Override
        public HashTableEntry<K, V> next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            HashTableEntry<K, V> entry =
                    new HashTableEntry<>(keys[currentIndex], values[currentIndex]);
            currentIndex++;
            return entry;
        }
    }
}
