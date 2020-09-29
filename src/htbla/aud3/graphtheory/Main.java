package htbla.aud3.graphtheory;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.read(new File("../files/Flussproblem.csv"));
    }
}