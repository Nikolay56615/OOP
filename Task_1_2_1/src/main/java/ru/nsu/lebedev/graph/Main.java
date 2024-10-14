package ru.nsu.lebedev.graph;

import java.util.List;

/**
 * Main class.
 */
public class Main {
    /**
     * Main function.
     */
    public static void main(String[] args) {
        AdjacentMatrixGraph<String> graph1 = new AdjacentMatrixGraph<>();
        IncidenceMatrixGraph<String> graph2 = new IncidenceMatrixGraph<>();
        Graph.readDataForGraphFromFile(graph1, "file.txt");
        Graph.readDataForGraphFromFile(graph2, "file.txt");
        List<String> sorted1 = graph1.topSort("A");
        List<String> sorted2 = graph2.topSort("A");
        System.out.println("Topological Sort1: " + sorted1);
        System.out.println("Topological Sort2: " + sorted2);
    }
}