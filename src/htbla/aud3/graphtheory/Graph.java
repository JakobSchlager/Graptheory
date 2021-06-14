package htbla.aud3.graphtheory;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author TODO Bitte Gruppenmitglieder eintragen!
 */
public class Graph {
    private double currentShortestPath = 10000.0;
    private static final int ONEWAYROAD_THREASHOLD = 2000;
    public static List<ArrayList<Node>> nodes = new ArrayList<>();
    public static List<ArrayList<Node>> fluss = new ArrayList<>();

    public int maxFlow = 2000;
    /*public void read(File adjacencyMatrix)  {
        try {
            List<String> lines = Files.lines(adjacencyMatrix.getAbsoluteFile().toPath()).collect(Collectors.toList());
            lines.forEach(line -> {
                List<String> connections = Arrays.asList(line.split(";"));
                ArrayList<Node> connectionsPerNode = new ArrayList<>();
                for (int i = 0; i < connections.size(); i++) {
                    int connectionIntValue = Integer.parseInt(connections.get(i));
                    if (connectionIntValue != 0) connectionsPerNode.add(new Node(i, connectionIntValue));
                }
                nodes.add(connectionsPerNode);
            });
        } catch (IOException E) {
            System.out.println("IO Exception!");
        }
    }*/

    public void read(File adjacencyMatrix, boolean flussproblem) {
        try {
            List<String> lines = Files.lines(adjacencyMatrix.getAbsoluteFile().toPath()).collect(Collectors.toList());
            lines.forEach(line -> {
                List<String> connections = Arrays.asList(line.split(";"));
                ArrayList<Node> connectionsPerNode = new ArrayList<>();
                for (int i = 0; i < connections.size(); i++) {
                    int connectionIntValue = Integer.parseInt(connections.get(i));
                    if (connectionIntValue != 0) connectionsPerNode.add(new Node(i, connectionIntValue));
                }
                if (flussproblem) fluss.add(connectionsPerNode);
                else nodes.add(connectionsPerNode);
            });
        } catch (IOException E) {
            System.out.println("IO Exception!");
        }
    }

    public List<List<Node>> read(File adjacencyMatrix) {
        List<List<Node>> temp = new ArrayList();

        try {
            List<String> lines = Files.lines(adjacencyMatrix.getAbsoluteFile().toPath()).collect(Collectors.toList());
            lines.forEach(line -> {
                List<String> connections = Arrays.asList(line.split(";"));
                ArrayList<Node> connectionsPerNode = new ArrayList<>();
                for (int i = 0; i < connections.size(); i++) {
                    int connectionIntValue = Integer.parseInt(connections.get(i));
                    if (connectionIntValue != 0) connectionsPerNode.add(new Node(i, connectionIntValue));
                }
                temp.add(connectionsPerNode);
            });
        } catch (IOException E) {
            System.out.println("IO Exception!");
        }

        return temp;
    }

    public static List<ArrayList<Node>> getNodes() {
        return nodes;
    }

    public Path determineShortestPath(int sourceNodeId, int targetNodeId) {
        return recursivePathSearch(sourceNodeId, targetNodeId, new Path().setNodeIds(new int[]{sourceNodeId}));
    }

    public Path determineShortestPath(int sourceNodeId, int targetNodeId, int... viaNodeIds) {
        //adding the sourcenode id at the beginning and targetNodeId at the end of the array
        int[] nodeIds = new int[viaNodeIds.length + 2];
        nodeIds[0] = sourceNodeId;
        for (int i = 0; i < viaNodeIds.length; i++) nodeIds[i + 1] = viaNodeIds[i];
        nodeIds[nodeIds.length - 1] = targetNodeId;

        //creating a path from one node to the next
        Path result = new Path().setNodeIds(new int[]{});
        for (int i = 0; i < nodeIds.length - 1; i++) {
            result.addNodeIds(recursivePathSearch(nodeIds[i], nodeIds[i + 1], new Path().setNodeIds(new int[]{nodeIds[i]})).getNodeIds());
            Path path = recursivePathSearch(nodeIds[i], nodeIds[i + 1], new Path().setNodeIds(new int[]{nodeIds[i]}));
            for (int node : path.getNodeIds()) {
                System.out.print(node);
            }
            System.out.println(":" + path.computeDistance());
        }
        return result;
    }

    private Path recursivePathSearch(int sourceNodeId, int targetNodeId, Path callers) {
        //currentShortestPath ist der kürtzeste Weg der aktuell gefuden wurde, ist distance größer ist weitere suche sinnlos
        if (callers.computeDistance() > currentShortestPath) return null;

        //Abbruchsbedingung
        if (sourceNodeId == targetNodeId) {
            if (currentShortestPath > callers.computeDistance()) currentShortestPath = callers.computeDistance();
            return callers;
        }

        //inMethodShortestPath ist der kürzeste Weg von sourceNodeId zu targetNodeId
        Path tempCallers;
        Path inMethodShortestPath = null;
        for (Node node : nodes.get(sourceNodeId).stream().sorted(Comparator.comparingInt(Node::getDistance)).collect(Collectors.toList())) {
            if (Arrays.stream(callers.getNodeIds()).noneMatch(nodeId -> nodeId == node.getTargetNode())) {
                //temp callers muss jedes mal auf den wert von nodeIds zurückgesetzt werden, da sonst von dem letzten methodenaufruf/Weg die Knoten gespeichert bleiben würden
                tempCallers = new Path().setNodeIds(callers.nodeIds);
                Path tempShortestPath = recursivePathSearch(node.getTargetNode(), targetNodeId, tempCallers.addNodeIds(new int[]{node.getTargetNode()}));
                //es wird überprüft ob ein weg möglich ist (!= null) und ob tempShortestPath kleiner ist als der bis jetzt in der recursion gemessenen kürzeste weg (= in MethodShortestPath)
                if (inMethodShortestPath == null || (tempShortestPath != null && tempShortestPath.computeDistance() < inMethodShortestPath.computeDistance()))
                    inMethodShortestPath = tempShortestPath;
            }
        }
        return inMethodShortestPath;
    }

    class Node {
        private final int targetNode;
        private int distance;

        public Node(int targetNode, int distance) {
            this.targetNode = targetNode;
            this.distance = distance;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance_in) {
            distance = distance_in;
        }

        public int getTargetNode() {
            return targetNode;
        }
    }

    public void determineMaximumFlow(int from, int to) {
        from --;
        to --;

        //determine shortest path und alle flüsse neu setzen
        //determine shortest path
        //wenn alle nodes die bei der zielnode ankommen einen fluss von 0 haben, ende
        int owThC = 0;
        int overAllFluss = 0;

        List<List<Node>> defaultFluss = read(new File("src\\htbla\\aud3\\graphtheory\\files\\Linz_Flussproblem.csv"));

        while(true) {
            //Abbruchbedingung: Wenn der fluss für die wege zu der Zielnode 0 gepseichert hat, beenden
            //kann verbessert werden indem die zu überprüfenden nodes vor der while schleife gemerkt werden
            boolean allWaysAusgelasted = true;
            for (int i = 0; i < fluss.size(); ++i) {
                for (Node n : fluss.get(i)) {
                    if(n.targetNode == to && n.getDistance() > 0 && ONEWAYROAD_THREASHOLD >= owThC) //onewayThreashold: keine gute lösung aber lösung
                        allWaysAusgelasted = false;
                }
            }
            if (allWaysAusgelasted) break;

            currentShortestPath = 10000; //damit weitergesucht wird sonst immer Abbruchbedingung
            Path path = recursivePathSearchFlow(from, to, new Path().setNodeIds(new int[]{from}), 2000);

            //Path p1 = recursivePathSearchFlow(from, to, new Path().setNodeIds(new int[]{from}), 2000);
            //System.out.println();

            //herausfinden was der punkt vor dem ziel war, um den weg davon finden zu können und damit die anzahl der autos welche darüber gefahren sind
            if(path != null) {
                int[] res = path.getNodeIds();
                int pathFrom = res[res.length - 2];

                //fluss upgdaten:
                //path von bis path bis durchgehen und fluss merken
                //von links nach rechts immer den kürzesten fluss subtrahieren

                List<Integer> flow = new ArrayList<>();

                for (int i = 0; i < res.length - 1; ++i) {
                    List<Node> fromNode = fluss.get(res[i]);
                    int toNode = res[i + 1];

                    Node destinyNode = null;
                    for (Node n : fromNode) {
                        if (n.targetNode == toNode) destinyNode = n;
                    }

                    flow.add(destinyNode.getDistance());
                }

                if (flow.size() == 1) flow.set(0, 0);
                else {
                    for (int i = 0; i < flow.size() - 1; i++) {
                        if (flow.get(i) >= flow.get(i + 1)) {
                            flow.set(i, flow.get(i) - flow.get(i + 1));
                            if (i == flow.size() - 2) flow.set(flow.size() - 1, 0);
                        } else {
                            flow.set(i + 1, flow.get(i + 1) - flow.get(i));
                            flow.set(i, 0);
                        }
                    }
                }

                for (int i = 0; i < res.length - 1; ++i) {
                    List<Node> fromNode = fluss.get(res[i]);
                    int toNode = res[i + 1];

                    Node destinyNode = null;
                    for (Node n : fromNode) {
                        if (n.targetNode == toNode) destinyNode = n;
                    }

                    destinyNode.setDistance(flow.get(i));
                }

                int auslastungNeu = -1;
                for (Node n : fluss.get(pathFrom)) {
                    if (n.getTargetNode() == to) auslastungNeu = n.getDistance();
                }

                int auslastungALt = -1;
                for (Node n : defaultFluss.get(pathFrom)) {
                    if (n.getTargetNode() == to) auslastungALt = n.getDistance();
                }

                if (auslastungALt == -1 || auslastungNeu == -1) System.out.println("Proooblem!!!");
                int tempCars = auslastungALt - auslastungNeu; //man muss überprüfen wie viele autos der weg wirklch durchgelassen hat

                System.out.println("tempFluss:" + tempCars);

                overAllFluss += tempCars; //die autos welche über den weg gekommen sind werden zu dem insgesamten fluss addiert!
            }
            owThC++;
        }

        System.out.println("OverallFluss: " + overAllFluss);
        fluss = read(new File("src\\htbla\\aud3\\graphtheory\\files\\Linz_Flussproblem.csv"));
    }

    private Path recursivePathSearchFlow(int sourceNodeId, int targetNodeId, Path callers, int cars) {
        //currentShortestPath ist der kürtzeste Weg der aktuell gefuden wurde, ist distance größer ist weitere suche sinnlos
        if (callers.computeDistance() > currentShortestPath) return null;

        //Abbruchsbedingung
        if (sourceNodeId == targetNodeId) {
            if (currentShortestPath > callers.computeDistance()) currentShortestPath = callers.computeDistance();
            return callers;
        }

        //inMethodShortestPath ist der kürzeste Weg von sourceNodeId zu targetNodeId
        Path tempCallers;
        Path inMethodShortestPath = null;
        int tempCars = 0;
        List<Node> relevantNodes = nodes.get(sourceNodeId);

        for (int i = 0; i < relevantNodes.size(); i++) {
            Node node = relevantNodes.get(i);
            if (Arrays.stream(callers.getNodeIds()).noneMatch(nodeId -> nodeId == node.getTargetNode())) { //loops verhindern, also falls schon einaml dortt war, nichtnoch einmal
                if(fluss.get(sourceNodeId).get(i).getDistance() > 0) { //wenn der aktulle weeg keinen platz für zusätzliche autos hat, ist der weg nicht möglich
                    tempCars = fluss.get(sourceNodeId).get(i).getDistance(); //tempCars = anzahl von autos die auf dieser straße fahren können
                    if (cars < tempCars)
                        tempCars = cars; //wenn weniger autos hereingefahren sind als hier verfügbar, dann nur diese autos zählen

                    //temp callers muss jedes mal auf den wert von nodeIds zurückgesetzt werden, da sonst von dem letzten methodenaufruf/Weg die Knoten gespeichert bleiben würden
                    tempCallers = new Path().setNodeIds(callers.nodeIds);
                    Path tempShortestPath = recursivePathSearchFlow(node.getTargetNode(), targetNodeId, tempCallers.addNodeIds(new int[]{node.getTargetNode()}), tempCars);
                    //es wird überprüft ob ein weg möglich ist (!= null) und ob tempShortestPath kleiner ist als der bis jetzt in der recursion gemessenen kürzeste weg (= in MethodShortestPath)
                    if (inMethodShortestPath == null || (tempShortestPath != null && tempShortestPath.computeDistance() < inMethodShortestPath.computeDistance())) {
                        inMethodShortestPath = tempShortestPath;

                        //fluss call by reference :(
                    }
                }
            }
        }
        return inMethodShortestPath;
    }



    /*public int recursiveMaxFlowSearch(Node in_node, int to, int currentMaxFlow, ArrayList<Integer> callers) {
        int from = in_node.getTargetNode();
        if(from == to) return currentMaxFlow;

        ArrayList<Integer> tempCallers;
        int inMethodMaxFluss = 0;
        for (int i = 0; i < fluss.get(from).size(); i++) {
            Node node = fluss.get(from).get(i);

            tempCallers = callers;
            tempCallers.add(from);
            if(!tempCallers.contains(node.getTargetNode())) {
                int tempMaxFluss = recursiveMaxFlowSearch(node, to, node.getDistance(), tempCallers);
                if (tempMaxFluss > inMethodMaxFluss) {
                    inMethodMaxFluss = tempMaxFluss;
                    fluss.get(tempCallers.size() - 2).get(from).setDistance(in_node.distance - node.getDistance());
                }
            }
        }

        return inMethodMaxFluss;
    }*/

    //correct logic for Flussproblem

    public int recursiveFlussproblem(Node old_node, int to, int last_from) {
        int from = old_node.getTargetNode();
        if (from == to) return old_node.getDistance();

        int inMethodFluss = 0;


        for (Node new_node : fluss.get(from)) {
            //if(last_from != new_node.getTargetNode()) {

                if (old_node.getDistance() < new_node.getDistance()) new_node.setDistance(old_node.getDistance());

                int res = 0;
                if (new_node.getDistance() > 0) res = recursiveFlussproblem(new_node, to, from);

                inMethodFluss += res;
                old_node.setDistance(old_node.getDistance() - res);
            //}
        }

        return inMethodFluss;
    }
}
/*public int recursiveFlussproblem(Node old_node, int to, List<Integer> callers) {
        int from = old_node.getTargetNode();
        if (from == to) return old_node.getDistance();

        int inMethodFluss = 0;

        List<Integer> tempCallers = callers;
        tempCallers.add(from);
        for (Node new_node : fluss.get(from)) {
            if(from == to) break;

            if(old_node.getDistance() < new_node.getDistance()) new_node.setDistance(old_node.getDistance());

            int res = 0;
            if(new_node.getDistance() > 0) res = recursiveFlussproblem(new_node, to, tempCallers);

            inMethodFluss += res;
            old_node.setDistance(old_node.getDistance() - res);
        }

        return inMethodFluss;
    }*/

    //angekommen: merke den fluss

    //für jeden möglichen weg:
    //-zurückgehen verbieten!
    //-überprüfen ob vorherige node noch flussmenge hat,
    //  falls old_node.flussmenge > new_node.flussmenge -> old_node.flussmenge = old_node.flussmenge - new_node.flussmenge;
    //  falls new_node.flussmenge > old_node.flussmenge -> new_node.flussmenge = old_node.flussmenge;
    //falls nicht kann beendet werden
    //-recusrive durchführung





    //ohne path:
    /*private int recursivePathSearch(int sourceNodeId, int targetNodeId, int distance, int caller) {
        //currentShortestPath ist der kürzesze Weg der aktuell gefuden wurde, ist distance größer ist weitere suche sinnlos
        if(distance > currentShortestPath) return Integer.MAX_VALUE;
        //Abbruchsbedingungwfws
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
    }*/

    /*public List<List<Integer>> cartesianProduct(int index, List<Integer>... list) {
        List<List<Integer>> ret = new ArrayList<>();
        if (index == list.length) {
            ret.add(new ArrayList<Integer>());
        } else {
            for (Integer integer : list[index]) {
                for (List<Integer> innerRecusion : cartesianProduct(index+1, list)) {
                    if(!innerRecusion.contains(integer)) innerRecusion.add(integer);
                    ret.add(innerRecusion);
                }
            }
        }
        return ret;
    }*/

