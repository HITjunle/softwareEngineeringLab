import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomGraphTest {
    private CustomGraph graph;


    private void setUpBridge() {
        graph = new CustomGraph();
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        String file = "Text/2.txt";
        String filePath = currentDir.resolve(file).toString();
        String[] words = CustomGraph.readFile(filePath);


        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            if (!word1.isEmpty() && !word2.isEmpty()) {
                graph.addVertex(word1);
                graph.addVertex(word2);
                graph.addEdge(word1, word2);
            }
        }
    }

    public void setUpShortestPath() {
        graph = new CustomGraph();
        // Setup nodes and connections here to ensure test isolation
        graph.addVertex("nodeA");
        graph.addVertex("nodeB");
        graph.addVertex("nodeC");
        graph.addEdge("nodeA", "nodeB");
        graph.addEdge("nodeB", "nodeC");
    }

    @Test
    public void testQueryBridgeWords_ExistingBridgeWords() {
        setUpBridge();
        assertEquals("better", graph.queryBridgeWords("a", "future"));
    }

    @Test
    public void testQueryBridgeWords_NoBridgeWords() {
        setUpBridge();
        assertEquals("No bridge words from do to you!", graph.queryBridgeWords("Do", "you"));
    }

    @Test
    public void testQueryBridgeWords_CaseInsensitive() {
        setUpBridge();
        assertEquals("better", graph.queryBridgeWords("A", "Future"));
    }

    @Test
    public void testQueryBridgeWords_BothWordsNotInGraph() {
        setUpBridge();
        assertEquals("No bridge words from ha to ha!", graph.queryBridgeWords("ha", "ha"));
    }

    @Test
    public void testQueryBridgeWords_OneWordNotInGraph() {
        setUpBridge();
        assertEquals("No bridge words from ha to a!", graph.queryBridgeWords("ha", "a"));
    }

    @Test
    public void testPathExists() {
        // Test scenario where path exists
        setUpShortestPath();
        String result = graph.calcShortestPath("nodeA", "nodeC");
        assertEquals("nodea -> nodeb -> nodec", result);
    }

    @Test
    public void testPathDoesNotExist() {
        // Test scenario where no path exists
        setUpShortestPath();
        graph.addVertex("isolatedNode");
        String result = graph.calcShortestPath("isolatedNode", "nodeA");
        assertEquals("No path from isolatednode to nodea", result);
    }

    @Test
    public void testNoPathResponse() {
        // Test the response when one or both nodes are not in the graph
        setUpShortestPath();
        String result = graph.calcShortestPath("unknownNode", "nodeA");
        assertEquals("No path from unknownnode to nodea", result);
    }

    @Test
    public void testDirectPath() {
        // Test the shortest path when both nodes are the same
        setUpShortestPath();
        String result = graph.calcShortestPath("nodeA", "nodeA");
        assertEquals("nodea", result);
    }

    @Test
    public void testPathWithMultipleOptions() {
        // Test where multiple paths exist and shortest is expected
        setUpShortestPath();
        graph.addVertex("nodeD");
        graph.addEdge("nodeA", "nodeD");
        graph.addEdge("nodeD", "nodeC");
        String result = graph.calcShortestPath("nodeA", "nodeC");
        assertEquals("nodea -> noded -> nodec", result); // Assuming this is the shorter path based on weights not shown here
    }
}
