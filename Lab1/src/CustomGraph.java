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

    String queryBridgeWords(String word1, String word2) {
        if (!adjacencyMap.containsKey(word1) || !adjacencyMap.containsKey(word2)) {
            return "No bridge words from word1 to word2!";
        }

        // word1->string, string->word2 其中string为bridge word
        StringBuilder bridgeWords = new StringBuilder();
        for (String bridgeWord : adjacencyMap.get(word1).keySet()) {
            if (adjacencyMap.get(bridgeWord).containsKey(word2)) {
                bridgeWords.append(bridgeWord).append(" ");
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge words from word1 to word2!";
        } else {
            return bridgeWords.toString();
        }
    }
    String generateNewText(String inputText){
        // 输入新样本，根据brige word生成新样本
        String[] words = inputText.split("\\s+");
        StringBuilder newText = new StringBuilder();
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            newText.append(word1).append(" ");
            if (!word1.isEmpty() && !word2.isEmpty()) {
                String bridgeWords = queryBridgeWords(word1, word2);
                if (!bridgeWords.isEmpty()) {
                    String[] bridgeWordsArray = bridgeWords.split("\\s+");
                    newText.append(bridgeWordsArray[(int) (Math.random() * bridgeWordsArray.length)]).append(" ");
                }
            }
        }
        newText.append(words[words.length - 1]);
        return newText.toString();
    }

    String calcShortestPath(String word1, String word2) {
        // 计算最短路径
        if (!adjacencyMap.containsKey(word1) || !adjacencyMap.containsKey(word2)) {
            return "No path from word1 to word2!";
        }


    }
//    public void drawGraph() {
//        SingleGraph graph = new SingleGraph("Custom Graph");
//        for (String vertex : adjacencyMap.keySet()) {
//            graph.addNode(vertex);
//        }
//
//        for (String vertex : adjacencyMap.keySet()) {
//            for (Map.Entry<String, Integer> entry : adjacencyMap.get(vertex).entrySet()) {
//                graph.addEdge(vertex + entry.getKey(), vertex, entry.getKey(), true);
//            }
//        }
//
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
        CustomGraph customGraph = new CustomGraph();
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        String file = "Text/1.txt";
        String filePath = currentDir.resolve(file).toString();
        String[] words = readFile(filePath);

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
        // customGraph.drawGraph();
        System.out.println(customGraph.queryBridgeWords("to", "out"));
        System.out.println(customGraph.generateNewText("Seek to explore new and exciting synergies"));
    }
}
