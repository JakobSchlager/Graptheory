package htbla.aud3.graphtheory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TODO Bitte Gruppenmitglieder eintragen!
 */
public class Graph {
    private double currentShortestPath = 10000.0;
    //private int currentShortestPath = 10000;
    public static List<ArrayList<Node>> nodes = new ArrayList<>();

    public void read(File adjacencyMatrix) throws IOException {
        List<String> lines = Files.lines(adjacencyMatrix.getAbsoluteFile().toPath()).collect(Collectors.toList());
        lines.forEach(line -> {
            List<String> connections = Arrays.asList(line.split(";"));
            ArrayList<Node> connectionsPerNode = new ArrayList<>();
            for (int i = 0; i < connections.size(); i++) {
                int connectionIntValue = Integer.parseInt(connections.get(i));
                if(connectionIntValue != 0)  connectionsPerNode.add(new Node(i, connectionIntValue));
            }
            nodes.add(connectionsPerNode);
        });
    }

    public static List<ArrayList<Node>> getNodes() {
        return nodes;
    }

    public Path determineShortestPath(int sourceNodeId, int targetNodeId) {
        //System.out.println(recursivePathSearch(sourceNodeId, targetNodeId, 0, 0));
        Path path = recursivePathSearch(sourceNodeId, targetNodeId, new Path().setNodeIds(new int[]{sourceNodeId}));
        System.out.println(path.computeDistance());
        return path;
    }

    private Path recursivePathSearch(int sourceNodeId, int targetNodeId, Path callers) {
        //currentShortestPath ist der kürtzeste Weg der aktuell gefuden wurde, ist distance größer ist weitere suche sinnlos
        if(callers.computeDistance() > currentShortestPath) return null;

        //Abbruchsbedingung
        if(sourceNodeId == targetNodeId) {
            if(currentShortestPath > callers.computeDistance()) currentShortestPath = callers.computeDistance();
            return callers;
        }

        //inMethodShortestPath ist der kürzeste Weg von sourceNodeId zu targetNodeId
        Path tempCallers;
        Path inMethodShortestPath = null;
        for (Node node : nodes.get(sourceNodeId).stream().sorted(Comparator.comparingInt(Node::getDistance)).collect(Collectors.toList())) {
            if(Arrays.stream(callers.getNodeIds()).noneMatch(nodeId -> nodeId == node.getTargetNode())) {
                //temp callers muss jedes mal auf den wert von nodeIds zurückgesetzt werden, da sonst von dem letzten methodenaufruf/Weg die Knoten gespeichert bleiben würden
                tempCallers = new Path().setNodeIds(callers.nodeIds);
                Path tempShortestPath = recursivePathSearch(node.getTargetNode(), targetNodeId, tempCallers.addNodeIds(new int[]{node.getTargetNode()}));
                //es wird überprüft ob ein weg möglich ist (!= null) und ob tempShortestPath kleiner ist als der bis jetzt in der recursion gemessenen kürzeste weg (= in MethodShortestPath)
                if (inMethodShortestPath == null || (tempShortestPath != null && tempShortestPath.computeDistance() < inMethodShortestPath.computeDistance())) inMethodShortestPath = tempShortestPath;
            }
        }
        return inMethodShortestPath;
    }

    private int recursivePathSearch(int sourceNodeId, int targetNodeId, int distance, int caller) {
        //currentShortestPath ist der kürzesze Weg der aktuell gefuden wurde, ist distance größer ist weitere suche sinnlos
        if(distance > currentShortestPath) return Integer.MAX_VALUE;
        //Abbruchsbedingung
        if(sourceNodeId == targetNodeId) {
            if(currentShortestPath > distance) currentShortestPath = distance;
            return distance;
        }

        //inMethodShortestPath ist der kürzeste Weg von sourceNodeId zu targetNodeId
        int inMethodShortestPath = Integer.MAX_VALUE;
        for (Node node : nodes.get(sourceNodeId).stream().sorted(Comparator.comparingInt(Node::getDistance)).collect(Collectors.toList())) {
            if(caller != node.getTargetNode()) {
                int tempShortestPath = recursivePathSearch(node.getTargetNode(), targetNodeId, distance + node.getDistance(), sourceNodeId);
                if (tempShortestPath < inMethodShortestPath) inMethodShortestPath = tempShortestPath;
            }
        }
        return inMethodShortestPath;
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

    //1. Lösung mit "attempts"
    /*private int recursivePathSearch(int sourceNodeId, int targetNodeId, int distance, int caller, int attempt) {
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
    }*/
}
