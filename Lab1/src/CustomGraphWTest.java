import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class CustomGraphWTest {
    private CustomGraph graph;

    @Before
    public void setUp() {
        graph = new CustomGraph();
        // Setup nodes and connections here to ensure test isolation
        graph.addVertex("nodeA");
        graph.addVertex("nodeB");
        graph.addVertex("nodeC");
        graph.addEdge("nodeA", "nodeB");
        graph.addEdge("nodeB", "nodeC");
    }

    @Test
    public void testPathExists() {
        // Test scenario where path exists
        String result = graph.calcShortestPath("nodeA", "nodeC");
        assertEquals("nodea -> nodeb -> nodec", result);
    }

    @Test
    public void testPathDoesNotExist() {
        // Test scenario where no path exists
        graph.addVertex("isolatedNode");
        String result = graph.calcShortestPath("isolatedNode", "nodeA");
        assertEquals("No path from isolatednode to nodea", result);
    }

    @Test
    public void testNoPathResponse() {
        // Test the response when one or both nodes are not in the graph
        String result = graph.calcShortestPath("unknownNode", "nodeA");
        assertEquals("No path from unknownnode to nodea", result);
    }

    @Test
    public void testDirectPath() {
        // Test the shortest path when both nodes are the same
        String result = graph.calcShortestPath("nodeA", "nodeA");
        assertEquals("nodea", result);
    }

    @Test
    public void testPathWithMultipleOptions() {
        // Test where multiple paths exist and shortest is expected
        graph.addVertex("nodeD");
        graph.addEdge("nodeA", "nodeD");
        graph.addEdge("nodeD", "nodeC");
        String result = graph.calcShortestPath("nodeA", "nodeC");
        assertEquals("nodea -> noded -> nodec", result); // Assuming this is the shorter path based on weights not shown here
    }
}
