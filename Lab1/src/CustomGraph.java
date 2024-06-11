import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;


/**
 * CustomGraph class represents a directed graph that can be used to perform various operations
 * such as finding bridge words,
 * generating new text, and calculating the shortest path between two words.
 * The graph is represented as an adjacency list where each vertex
 * is mapped to a list of its neighbors along with the edge weights.
 */
public class CustomGraph {

    private Map<String, Map<String, Integer>> adjacencyMap;

    // Enhanced styling
    String stylesheet =
            "node {"
                    + "   shape: circle;"
                    + "   size: 30px;"
                    + "   fill-color: #1f78b4;"
                    + "   text-size: 10px;"
                    + "   text-color: white;"
                    + "   text-style: bold;"
                    + "}"
                    + "edge {"
                    + "   shape: line;"
                    + "   size: 2px;"
                    + "   fill-color: #33a02c;"
                    + "   arrow-size: 5px, 4px;"
                    + "   text-size: 10px;"
                    + "   text-background-mode: rounded-box;"
                    + "   text-background-color: white;"
                    + "   text-padding: 3px;"
                    + "   text-offset: 5px, 0px;"
                    + "}";

    public CustomGraph() {
        this.adjacencyMap = new HashMap<>();
    }

    public void addVertex(String vertex) {
        adjacencyMap.putIfAbsent(vertex.toLowerCase(), new HashMap<>());
    }

    public void addEdge(String from, String to) {
        Map<String, Integer> neighbors = adjacencyMap.get(from.toLowerCase());
        neighbors.put(to.toLowerCase(), neighbors.getOrDefault(to, 0) + 1);
    }

    /**
     * Prints the graph in the form of adjacency list.
     */
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
        if (!adjacencyMap.containsKey(word1.toLowerCase())
                || !adjacencyMap.containsKey(word2.toLowerCase())) {
            return "No bridge words from "
                    + word1.toLowerCase() + " to " + word2.toLowerCase() + "!";
        }

        // word1->string, string->word2 其中string为bridge word
        StringBuilder bridgeWords = new StringBuilder();
        for (String bridgeWord : adjacencyMap.get(word1.toLowerCase()).keySet()) {
            if (adjacencyMap.get(bridgeWord).containsKey(word2.toLowerCase())) {
                bridgeWords.append(bridgeWord).append(" ");
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge words from "
                    + word1.toLowerCase() + " to " + word2.toLowerCase() + "!";
        } else {
            return bridgeWords.toString().trim();
        }
    }

    /**
     * Generates new text by inserting bridge words between adjacent words in the input text.
     *
     *@param inputText The input text to generate new text from.
     *@return The new text with bridge words inserted between adjacent words.
     **/
    public String generateNewText(String inputText) {
        String[] words = inputText.replaceAll("[^a-zA-Z ]", "").split("\\s+");

        StringBuilder newText = new StringBuilder();

        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i].toLowerCase();
            String word2 = words[i + 1].toLowerCase();
            newText.append(word1).append(" ");

            String bridgeWords = queryBridgeWords(word1, word2);
            if (!bridgeWords.startsWith("No bridge words")) {
                String[] bridgeWordsArray = bridgeWords.split(" ");
                // import random
                Random random = new Random();
                int randomIndex = random.nextInt(bridgeWordsArray.length);
                newText.append(bridgeWordsArray[randomIndex]).append(" ");
            }
        }

        // 添加最后一个单词
        newText.append(words[words.length - 1]);

        return newText.toString();
    }


    /**
     * Calculates the shortest path between two words in the graph using Dijkstra's algorithm.
     *
     * @param word1 The starting word.
     * @param word2 The ending word.
     * @return The shortest path between the two words, or a message if no path exists.
     **/
    public String calcShortestPath(String word1, String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();
        if (!adjacencyMap.containsKey(word1) || !adjacencyMap.containsKey(word2)) {
            return "No path from " + word1 + " to " + word2;
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previousVertices = new HashMap<>();

        // 初始化距离和前驱节点
        for (String vertex : adjacencyMap.keySet()) {
            distances.put(vertex, Integer.MAX_VALUE); // 初始化为无穷大
            previousVertices.put(vertex, null); // 前驱节点初始化为null
        }
        distances.put(word1, 0); // 起始节点距离为0

        PriorityQueue<String> queue
                = new PriorityQueue<>((a, b) -> distances.get(a) - distances.get(b)); // 优先队列，按距离降序
        queue.offer(word1); // 将起始节点加入队列

        while (!queue.isEmpty()) {
            String currentVertex = queue.poll(); // 弹出距离最小的节点

            if (currentVertex.equals(word2)) {
                // 已找到最短路径,构建路径字符串
                List<String> path = new ArrayList<>();
                String vertex = word2;
                while (vertex != null) {
                    path.add(0, vertex);
                    vertex = previousVertices.get(vertex);
                }
                return String.join(" -> ", path);
            }

            Map<String, Integer> neighbors = adjacencyMap.get(currentVertex);
            for (String neighbor : neighbors.keySet()) {
                int altDistance = distances.get(currentVertex) + 1;
                if (altDistance < distances.get(neighbor)) {
                    distances.put(neighbor, altDistance);
                    previousVertices.put(neighbor, currentVertex);
                    queue.offer(neighbor);
                }
            }
        }

        return "No path from " + word1 + " to " + word2;
    }

    /**
     * Performs a random walk on the graph starting from a random vertex.
     * The user can stop the walk at any time by entering 'stop'.
     **/
    public void randomWalk() {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 'stop' to stop the random walk.");

        Random random = new Random();
        List<String> path = new ArrayList<>();
        Set<String> visitedEdges = new HashSet<>(); // 记录已访问的边

        // 选择一个随机起点
        String start = adjacencyMap.keySet()
                .stream().skip(random.nextInt(adjacencyMap.size())).findFirst().orElse(null);
        if (start == null) {
            System.out.println("The graph is empty.");
            return;
        }
        path.add(start);

        String current = start;
        while (true) {
            Map<String, Integer> neighbors = adjacencyMap.get(current);
            if (neighbors == null || neighbors.isEmpty()) {
                System.out.println("No neighbors found for: " + current);
                break;
            }

            List<String> neighborList = new ArrayList<>(neighbors.keySet());
            String next = neighborList.get(random.nextInt(neighborList.size()));
            String edge = current + "->" + next;

            if (visitedEdges.contains(edge)) {
                System.out.println("Repeated edge found: " + edge);
                break;
            }
            visitedEdges.add(edge);
            path.add(next);

            System.out.println("Current path: " + String.join(" -> ", path));
            System.out.print("Enter 'stop' to stop the random walk, or press Enter to continue: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("stop")) {
                break;
            }

            current = next;
        }

        System.out.println("Random walk path: " + String.join(" -> ", path));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("random_walk.txt"))) {
            writer.write("Random walk path: " + String.join(" -> ", path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示有向图.
     */
    public void showDirectedGraph() {
        System.setProperty("org.graphstream.ui", "swing");
        Graph streamGraph = new SingleGraph("Text Graph");

        // 用于构建控制台输出的图结构
        StringBuilder graphRepresentation = new StringBuilder();

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

                    // 在添加边时打印图结构
                    graphRepresentation.append(vertex)
                            .append(" -> ")
                            .append(neighbor)
                            .append(" (")
                            .append(weight)
                            .append(")\n");
                }
            }
        }

        // 打印图结构到控制台
        System.out.println(graphRepresentation.toString());
        // 设置布局参数，使图形更紧凑
        streamGraph.setAttribute("layout.force", 0.01); // 增加吸引力，使节点更靠近
        streamGraph.setAttribute("layout.repulsion", 0.01); // 减少排斥力，使节点更靠近
        streamGraph.setAttribute("ui.quality"); // 提高渲染质量
        streamGraph.setAttribute("ui.antialias"); // 开启抗锯形


        streamGraph.setAttribute("ui.stylesheet", stylesheet);
        streamGraph.setAttribute("ui.quality");
        streamGraph.setAttribute("ui.antialias");

        // Display the graph
        streamGraph.display();
    }



    // 重载方法，展现有向图并高亮最短路径
    /**
     * Displays the directed graph with the shortest path highlighted between two words.
     *
     * @param word1 The starting word.
     * @param word2 The ending word.
     **/
    public void showDirectedGraph(String word1, String word2) {
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

        streamGraph.setAttribute("ui.stylesheet", stylesheet);
        streamGraph.setAttribute("ui.quality");
        streamGraph.setAttribute("ui.antialias");


        // Highlight the shortest path
        String shortestPath = calcShortestPath(word1, word2);
        if (!shortestPath.startsWith("No path")) {
            String[] pathNodes = shortestPath.split(" -> ");
            for (int i = 0; i < pathNodes.length - 1; i++) {
                String edgeId = pathNodes[i] + "->" + pathNodes[i + 1];
                Edge edge = streamGraph.getEdge(edgeId);
                edge.setAttribute("ui.class", "highlighted");
            }

            int pathLength = 0;
            for (int i = 0; i < pathNodes.length - 1; i++) {
                String from = pathNodes[i];
                String to = pathNodes[i + 1];
                pathLength += adjacencyMap.get(from).get(to);
            }
            System.out.println("Shortest path length: " + pathLength);
        }

        // Display the graph
        streamGraph.display();
    }

    //javadoc
    /**
     * Reads the content of a file and returns an array of words.
     *
     * @param filePath The path to the file to read.
     * @return An array of words in the file.
     **/
    public static String[] readFile(String filePath) {
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

    /**
     * Main method to interact with the graph and perform various operations.
     *
     * @param args The command line arguments.
     **/
    @SuppressWarnings("checkstyle:FallThrough")
    public static void main(String[] args) {
        CustomGraph customGraph = new CustomGraph();
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        String file = "Text/2.txt";

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

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请选择一个操作：");
            System.out.println("1. 显示有向图");
            System.out.println("2. 查询桥接词");
            System.out.println("3. 最短路径");
            System.out.println("4. 随机游走");
            System.out.println("5. bridge word生成新文本");
            System.out.println("0. 退出");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 处理换行符

            switch (choice) {
                case 1:

                    customGraph.showDirectedGraph();
                    break;
                case 2:
                    System.out.print("输入两个单词以查询桥接词：");
                    String word1 = scanner.next();
                    String word2 = scanner.next();
                    System.out.println(customGraph.queryBridgeWords(word1, word2));
                    break;
                case 3:
                    System.out.print("输入两个单词以查询最短路径：");
                    word1 = scanner.next();
                    word2 = scanner.next();
                    System.out.println(customGraph.calcShortestPath(word1, word2));
                    customGraph.showDirectedGraph(word1, word2);
                    break;
                case 4:
                    customGraph.randomWalk();
                    break;
                case 5:
                    System.out.println("根据bridge word生成新文本，请输入文本：");
                    String bridgeWord = scanner.nextLine(); // 读取整行文本

                    System.out.println(customGraph.generateNewText(bridgeWord));
                    break;

                case 0:
                    System.out.println("退出程序");
                    System.exit(0);
                default:
                    System.out.println("无效选择，请重新选择。");
            }
        }
    }
}
