package ru.nsu.lebedev.graph;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Graph interface.
 *
 * @param <T> type of graph's vertices
 */
public interface Graph<T> {
    /**
     * Vertex getter.
     *
     * @param value value of vertex
     * @return `Vertex` object if it exists otherwise `null`
     */
    Vertex<T> getVertex(T value);

    /**
     * Vertex creation.
     * Add vertex with specified value if vertex with it doesn't exist.
     *
     * @param value value of vertex
     */
    void addVertex(T value);

    /**
     * Vertex removing.
     * Removes vertex and all incident edges if vertex exists.
     *
     * @param value value of vertex
     * @return deleted vertex or null if it doesn't exist
     */
    Vertex<T> removeVertex(T value);

    /**
     * Edge creation.
     * Creates edge (a,b) with specified weight if edge (a,b) doesn't exist.
     * Creates vertices `a` and `b` if they don't exist
     *
     * @param a first vertex
     * @param b second vertex
     * @param weight double value
     */
    void addEdge(T a, T b, double weight);

    /**
     * Edge removing.
     * Removes edge (a,b) if it exists.
     *
     * @param a first vertex value
     * @param b second vertex value
     * @return deleted edge or null if it doesn't exist
     */
    Edge<T> removeEdge(T a, T b);

    /**
     * Edge getter.
     *
     * @param a first vertex value
     * @param b second vertex value
     * @return edge (a, b) if it exists and `null` otherwise
     */
    Edge<T> getEdge(T a, T b);

    /**
     * Topological sorter.
     *
     * @param start vertex of started
     * @return list of vertices in topological sort
     */
    List<T> topSort(T start);

    /**
     * Method for reading graph's data from file.
     *
     * @param graph graph upcasted to Graph class.
     * @param filename file with data.
     */
    static void readDataForGraphFromFile(Graph<String> graph, String filename) {
        try (FileReader reader = new FileReader(filename)) {
            char[] buf = new char[50000];
            int len = reader.read(buf);
            var strings = String.copyValueOf(buf, 0, len).split("\r?\n");
            for (var string : strings) {
                var edgeData = string.split(" ");
                graph.addEdge(edgeData[0], edgeData[1], Double.parseDouble(edgeData[2]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
