import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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


    private static String[] readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Clean the content by removing non-alphabetic characters and converting to lowercase
        String cleanedContent = content.toString().replaceAll("[^a-zA-Z ]", " ").toLowerCase();

        // Split the cleaned content into words and return as an array
        return cleanedContent.split("\\s+");
    }
    public static void main(String[] args) {
        Graph graph = new Graph();
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        String file = "Text/1.txt";
        String filePath = currentDir.resolve(file).toString();
        String[] words = readFile(filePath);

        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            if (!word1.isEmpty() && !word2.isEmpty()) {
                graph.addVertex(word1);
                graph.addVertex(word2);
                graph.addEdge(word1, word2);
            }
        }

        graph.printGraph();


        graph.printGraph();

    }
}
