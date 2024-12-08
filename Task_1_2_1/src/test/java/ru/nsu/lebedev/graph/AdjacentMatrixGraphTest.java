package ru.nsu.lebedev.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing AdjacentMatrixGraph class.
 */
public class AdjacentMatrixGraphTest {
    private Graph<String> graph;

    /**
     * Function for creating default graph.
     */
    @BeforeEach
    public void setUp() throws Exception {
        graph = new AdjacentMatrixGraph<>();
        DefaultVertexReader reader = new DefaultVertexReader();
        Graph.readDataForGraphFromFile(graph, "file.txt", reader);
    }

    @Test
    void getVertexAdjacentMatrixGraph() throws Exception {
        assertEquals(new Vertex<String>("A"), graph.getVertex("A"));
    }

    @Test
    void addVertexAdjacentMatrixGraph() throws Exception {
        graph.addVertex("Q");
        assertEquals(new Vertex<String>("Q"), graph.getVertex("Q"));
    }

    @Test
    void removeVertexAdjacentMatrixGraph() throws Exception {
        Vertex<String> vertex = graph.removeVertex("A");
        assertNull(graph.getVertex("A"));
        assertEquals(new Vertex<String>("A"), vertex);
    }

    @Test
    void removeVertexNullAdjacentMatrixGraph() throws Exception {
        assertNull(graph.removeVertex("Z"));
    }

    @Test
    void getEdgeAdjacentMatrixGraph() throws Exception {
        assertEquals(new Edge<>("A", "B", 1.0), graph.getEdge("A", "B"));
    }

    @Test
    void addEdgeAdjacentMatrixGraph() throws Exception {
        graph.addEdge("A", "Z", 7.0);
        assertEquals(new Edge<>("A", "Z", 7.0), graph.getEdge("A", "Z"));
    }

    @Test
    void removeEdgeAdjacentMatrixGraph() throws Exception {
        Edge<String> edge = graph.removeEdge("A", "B");
        assertEquals(new Edge<>("A", "B", 1.0), edge);
        assertNull(graph.getEdge("A", "B"));
    }

    @Test
    void removeEdgeNullAdjacentMatrixGraph() throws Exception {
        graph.removeEdge("A", "Z");
        assertNull(graph.removeEdge("A", "Z"));
    }

    @Test
    void getVerticesAdjacentMatrixGraph() throws Exception {
        assertEquals(new ArrayList<>(
                Arrays.asList("A", "B", "C", "D", "E", "F")), graph.getVertices());
    }

    @Test
    void getAdjacentVerticesAdjacentMatrixGraph() throws Exception {
        assertEquals(new ArrayList<>(Arrays.asList("B", "C")), graph.getAdjacentVertices("A"));
    }
}