package htbla.aud3.graphtheory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        try {
            graph.read(new File("src\\htbla\\aud3\\graphtheory\\files\\Linz.csv"));
            for (int i : graph.determineShortestPath(0, 1).getNodeIds()) {
                System.out.println(i);
            }
            System.out.println(graph.determineShortestPath(0, 1).computeDistance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}