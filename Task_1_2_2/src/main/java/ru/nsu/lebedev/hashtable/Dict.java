package ru.nsu.lebedev.hashtable;

public class Dict<T, D> {
    final T key;
    final D value;

    public Dict(T key, D value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
