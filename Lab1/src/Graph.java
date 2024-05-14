import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.HashMap;
import java.util.Map;

public class Graph {
    private Map<String, Map<String, Integer>> adjacencyMap;

    public Graph() {
        this.adjacencyMap = new HashMap<>();
    }

    public void addVertex(String vertex) {
        adjacencyMap.putIfAbsent(vertex, new HashMap<>());
    }

    public void addEdge(String from, String to) {
        Map<String, Integer> neighbors = adjacencyMap.get(from);
        neighbors.put(to, neighbors.getOrDefault(to, 0) + 1);
    }

    public void printGraph() {
        for (String vertex : adjacencyMap.keySet()) {
            System.out.print(vertex + " -> ");
            for (Map.Entry<String, Integer> entry : adjacencyMap.get(vertex).entrySet()) {
                System.out.print(entry.getKey() + "(" + entry.getValue() + ") ");
            }
            System.out.println();
        }
    }

//    public void displayGraph() {
//        org.graphstream.graph.Graph graph = new SingleGraph("Text Graph");
//
//        // Add nodes and edges
//        for (String vertex : adjacencyMap.keySet()) {
//            if (graph.getNode(vertex) == null) {
//                graph.addNode(vertex).addAttribute("ui.label", vertex);
//            }
//            for (Map.Entry<String, Integer> entry : adjacencyMap.get(vertex).entrySet()) {
//                String neighbor = entry.getKey();
//                int weight = entry.getValue();
//                if (graph.getNode(neighbor) == null) {
//                    graph.addNode(neighbor).addAttribute("ui.label", neighbor);
//                }
//                String edgeId = vertex + "->" + neighbor;
//                if (graph.getEdge(edgeId) == null) {
//                    Edge edge = graph.addEdge(edgeId, vertex, neighbor, true);
//                    edge.addAttribute("weight", weight);
//                    edge.addAttribute("ui.label", weight);
//                }
//            }
//        }
//
//        // Style the graph
//        graph.addAttribute("ui.stylesheet", "node { text-size: 20px; } edge { text-size: 20px; }");
//        graph.addAttribute("ui.quality");
//        graph.addAttribute("ui.antialias");
//
//        // Display the graph
//        graph.display();
//    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        // Example input
        graph.addVertex("to");
        graph.addVertex("explore");
        graph.addVertex("strange");
        graph.addVertex("new");
        graph.addVertex("worlds");
        graph.addVertex("seek");
        graph.addVertex("out");
        graph.addVertex("life");
        graph.addVertex("and");
        graph.addVertex("civilizations");

        graph.addEdge("to", "explore");
        graph.addEdge("to", "seek");
        graph.addEdge("explore", "strange");
        graph.addEdge("strange", "new");
        graph.addEdge("new", "worlds");
        graph.addEdge("seek", "out");
        graph.addEdge("out", "new");
        graph.addEdge("new", "life");
        graph.addEdge("life", "and");
        graph.addEdge("and", "new");
        graph.addEdge("new", "civilizations");

        graph.printGraph();

    }
}
