package htbla.aud3.graphtheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author TODO Bitte Gruppenmitglieder eintragen!
 */
public class Path {

    int[] nodeIds;

    public Path setNodeIds(int[] nodeIds) { this.nodeIds = nodeIds; return this; }

    public Path addNodeIds(int[] nodeIds) {
        int[] result = Arrays.copyOf(this.nodeIds, this.nodeIds.length+nodeIds.length);
        System.arraycopy(nodeIds, 0, result, this.nodeIds.length, nodeIds.length);

        this.nodeIds = result;
        return this;
    }

    public int[] getNodeIds() { return nodeIds; }

    public double computeDistance() {
        int distance = 0;

        List<ArrayList<Graph.Node>> nodes = Graph.getNodes();
        for (int i = 0; i < nodeIds.length - 1; i++) {
            List<Graph.Node> nodesOfNode = nodes.get(nodeIds[i]);
            for (int j = 0; j < nodesOfNode.size(); j++) {
                if (nodesOfNode.get(j).getTargetNode() == nodeIds[i+1]) {
                    distance+=nodesOfNode.get(j).getDistance();
                    break;
                }
            }
        }
        return distance;
    }

}
