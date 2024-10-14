package ru.nsu.lebedev.graph;


/**
 * Vertex class.
 *
 * @param <T> type of vertex's value.
 */
public record Vertex<T>(T value) {

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vertex<?>)) {
            return false;
        }
        Vertex<T> otherVertex = (Vertex<T>) obj;
        return value.equals(otherVertex.value);
    }

    @Override
    public String toString() {
        return String.format("Vertex %s", value.toString());
    }
}
