package htbla.aud3.graphtheory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author TODO Bitte Gruppenmitglieder eintragen!
 */
public class Graph {

    public void read(File adjacencyMatrix) {
            adjacencyMatrix.getPath().lines().forEach(line -> {
            List<String> lines = Arrays.asList(line.split(";"));
                System.out.println("test"+lines.get(0));
        });
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
