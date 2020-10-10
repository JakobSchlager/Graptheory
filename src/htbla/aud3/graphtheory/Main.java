package htbla.aud3.graphtheory;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        try {
            graph.read(new File("src\\htbla\\aud3\\graphtheory\\files\\Linz.csv"));
            Path result = graph.determineShortestPath(13,49);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}