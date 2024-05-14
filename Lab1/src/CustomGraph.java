import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CustomGraph {

    private Map<String, Map<String, Integer>> adjacencyMap;

    public CustomGraph() {
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

    //功能需求2：展现有向同
    public void showDirectedGraph() {
        System.setProperty("org.graphstream.ui", "swing");
        Graph streamGraph = new SingleGraph("Text Graph");

        // Add nodes and edges
        for (String vertex : adjacencyMap.keySet()) {
            if (streamGraph.getNode(vertex) == null) {
                streamGraph.addNode(vertex).setAttribute("ui.label", vertex);
            }
            for (Map.Entry<String, Integer> entry : adjacencyMap.get(vertex).entrySet()) {
                String neighbor = entry.getKey();
                int weight = entry.getValue();
                if (streamGraph.getNode(neighbor) == null) {
                    streamGraph.addNode(neighbor).setAttribute("ui.label", neighbor);
                }
                String edgeId = vertex + "->" + neighbor;
                if (streamGraph.getEdge(edgeId) == null) {
                    Edge edge = streamGraph.addEdge(edgeId, vertex, neighbor, true);
                    edge.setAttribute("weight", weight);
                    edge.setAttribute("ui.label", weight);
                }
            }
        }

        // Enhanced styling
        String stylesheet =
                "node {" +
                        "   shape: circle;" +
                        "   size: 67px;" +
                        "   fill-color: #1f78b4;" +
                        "   text-size: 15px;" +
                        "   text-color: white;" +
                        "   text-style: bold;" +
                        "}" +
                        "edge {" +
                        "   shape: line;" +
                        "   size: 2px;" +
                        "   fill-color: #33a02c;" +
                        "   arrow-size: 10px, 5px;" +
                        "   text-size: 15px;" +
                        "   text-background-mode: rounded-box;" +
                        "   text-background-color: white;" +
                        "   text-padding: 3px;" +
                        "   text-offset: 5px, 0px;" +
                        "}";

        streamGraph.setAttribute("ui.stylesheet", stylesheet);
        streamGraph.setAttribute("ui.quality");
        streamGraph.setAttribute("ui.antialias");

        // Display the graph
        streamGraph.display();
    }



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
        CustomGraph customGraph = new CustomGraph();
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        String file = "Text/1.txt"; // Update this path according to your file location
        String filePath = currentDir.resolve(file).toString();
        String[] words = readFile(filePath);

        if (words == null) {
            System.out.println("Error reading the file.");
            return;
        }

        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            if (!word1.isEmpty() && !word2.isEmpty()) {
                customGraph.addVertex(word1);
                customGraph.addVertex(word2);
                customGraph.addEdge(word1, word2);
            }
        }

        customGraph.printGraph();
        customGraph.showDirectedGraph();
    }
}
