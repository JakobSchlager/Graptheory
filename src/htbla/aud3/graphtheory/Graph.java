package htbla.aud3.graphtheory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author TODO Bitte Gruppenmitglieder eintragen!
 */
public class Graph {

    public void read(File adjacencyMatrix) throws IOException {
        System.out.println(adjacencyMatrix.exists());

        String lines = Files.lines(adjacencyMatrix.getAbsoluteFile().toPath()).reduce((allLines, line) -> allLines+=line).get();
        System.out.println(lines);
     }

    public Path determineShortestPath(int sourceNodeId, int targetNodeId) {
        return null;
    }

    public Path determineShortestPath(int sourceNodeId, int targetNodeId, int... viaNodeIds) {
        return null;
    }

    public double determineMaximumFlow(int sourceNodeId, int targetNodeId) {
        return -1.0;
    }

    public List<Edge> determineBottlenecks(int sourceNodeId, int targetNodeId) {
        return null;
    }

}
