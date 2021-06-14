package htbla.aud3.graphtheory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
            graph.read(new File("src\\htbla\\aud3\\graphtheory\\files\\Linz_Flussproblem.csv"), true);
            graph.read(new File("src\\htbla\\aud3\\graphtheory\\files\\Linz.csv"), false);

            for (int i : graph.determineShortestPath(0, 30).getNodeIds()) {
                System.out.println(i);
            }
        System.out.println("shortest Path:");
            System.out.println(graph.determineShortestPath(0, 27).computeDistance());
        System.out.println("------");

        System.out.println("Max flow");
        graph.determineMaximumFlow(0, 30);
    }
}