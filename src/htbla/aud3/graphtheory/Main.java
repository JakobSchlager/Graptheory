package htbla.aud3.graphtheory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    
    static float fitness;

    public static void main(String[] args) {
        Graph graph = new Graph();
            graph.read(new File("src\\htbla\\aud3\\graphtheory\\files\\Linz_Flussproblem.csv"), true);
            graph.read(new File("src\\htbla\\aud3\\graphtheory\\files\\Linz.csv"), false);
            for (int i : graph.determineShortestPath(0, 30).getNodeIds()) {
                System.out.println(i);
            }
            System.out.println(graph.determineShortestPath(0, 1).computeDistance());

        System.out.println("Max flow");
            //graph.determineMaximumFlow(0, 30);
         //graph.determineMaximumFlow(25, 26); //25 - 26
         //graph.determineMaximumFlow(29, 28); //29-28
         //graph.determineMaximumFlow(1, 2);

        /*int[] nodesinlayers = new int[5];

        for (int i = 0; i < nodesinlayers.length; i++) {
            System.out.println(nodesinlayers[i]);
        }*/

        float firtness;
        System.out.println(firtness);
    }
}