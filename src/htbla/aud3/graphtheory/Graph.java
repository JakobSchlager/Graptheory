package htbla.aud3.graphtheory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TODO Bitte Gruppenmitglieder eintragen!
 */
public class Graph {
    List<ArrayList<Node>> nodes = new ArrayList<ArrayList<Node>>();

    public void read(File adjacencyMatrix) throws IOException {
        System.out.println(adjacencyMatrix.exists());

        List<String> lines = Files.lines(adjacencyMatrix.getAbsoluteFile().toPath()).collect(Collectors.toList());
        lines.forEach(line -> {
            List<String> connections = Arrays.asList(line.split(";"));
            ArrayList<Node> connectionsPerNode = new ArrayList<>();
            for (int i = 0; i < connections.size(); i++) {
                Integer connectionIntValue = Integer.valueOf(connections.get(i));
                if(connectionIntValue != 0)  connectionsPerNode.add(new Node(i, connectionIntValue));
            }
            nodes.add(connectionsPerNode);
        });
    }

    public Path determineShortestPath(int sourceNodeId, int targetNodeId) {
        System.out.println(recursivePathSearch(52, 2, 0, 0, 0));

        return null;
    }

    private int recursivePathSearch(int sourceNodeId, int targetNodeId, int distance, int caller, int attempt) {
        if(attempt >= 20) return Integer.MAX_VALUE;
        if(sourceNodeId == targetNodeId) return distance;

        int overallDistance = Integer.MAX_VALUE;
            for (Node node : nodes.get(sourceNodeId)) {
                if(caller != node.getTargetNode()) {
                    int tempOverallDistance = recursivePathSearch(node.getTargetNode(), targetNodeId, distance + node.getDistance(), sourceNodeId, attempt+=1);
                    if (tempOverallDistance < overallDistance) overallDistance = tempOverallDistance;
                }
            }
            return overallDistance;
    }

    /* private int recursivePathSearchLoopDetection(int sourceNodeId, int targetNodeId, int distance, int caller, List<Integer> loopList) {
        //Treashhold evt. 20 anpassbar!
        if(isLoop(loopList)) return Integer.MAX_VALUE;
        if(sourceNodeId == targetNodeId) return distance;
        else {
            int overallDistance = Integer.MAX_VALUE;

            for (Node node : nodes.get(sourceNodeId)) {
                if(caller != node.getTargetNode()) {
                    loopList.add(node.getTargetNode());
                    int tempOverallDistance = recursivePathSearchLoopDetection(node.getTargetNode(), targetNodeId, distance + node.getDistance(), sourceNodeId, loopList);
                    if (tempOverallDistance < overallDistance) overallDistance = tempOverallDistance;
                }
            }
            return overallDistance;
        }
    }*/

    /*private boolean isLoop(List<Integer> loopList) {
        if(loopList.size() < 20) return false;

        int loopLength = 0;
        for (int i = 0; i < loopList.size(); i++) {
            for (int j = 0; j < loopList.size(); j++) {
                if(i != j && loopList.get(i) == loopList.get(j)) {

                        loopLength = i-j;
                    if(loopLength < 0) loopLength = loopLength * -1;

                    List<Integer> loopPart1;
                    List<Integer> loopPart2;
                    if(i > j) {
                        if(i+loopLength <= loopList.size()) return false;
                        loopPart1 = loopList.subList(i-loopLength, i);
                        loopPart2 = loopList.subList(i, i+loopLength);
                    } else {
                        if(j+loopLength <= loopList.size()) return false;
                        loopPart1 = loopList.subList(j-loopLength, j);
                        loopPart2 = loopList.subList(j, j+loopLength);
                    }

                    return loopPart1.equals(loopPart2);

                }
            }
        }

        return false;
    } */

    public Path determineShortestPath(int sourceNodeId, int targetNodeId, int... viaNodeIds) {
        return null;
    }

    public double determineMaximumFlow(int sourceNodeId, int targetNodeId) {
        return -1.0;
    }

    public List<Edge> determineBottlenecks(int sourceNodeId, int targetNodeId) {
        return null;
    }

    class Node {
        private int targetNode;
        private int distance;

        public Node(int targetNode, int distance) {
            this.targetNode = targetNode;
            this.distance = distance;
        }

        public int getDistance() {
            return distance;
        }

        public int getTargetNode() {
            return targetNode;
        }
    }
}
