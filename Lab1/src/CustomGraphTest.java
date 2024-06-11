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

    private String[] readFile(String filePath) {
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

    @Before
    public void setUp() {
        graph = new CustomGraph();
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        String file = "Lab1/Text/2.txt";
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
                graph.addVertex(word1);
                graph.addVertex(word2);
                graph.addEdge(word1, word2);
            }
        }
    }

    @Test
    public void testQueryBridgeWords_ExistingBridgeWords() {
        assertEquals("better", graph.queryBridgeWords("a", "future"));
    }

    @Test
    public void testQueryBridgeWords_NoBridgeWords() {
        assertEquals("No bridge words from do to you!", graph.queryBridgeWords("Do", "you"));
    }

    @Test
    public void testQueryBridgeWords_CaseInsensitive() {
        assertEquals("better", graph.queryBridgeWords("A", "Future"));
    }

    @Test
    public void testQueryBridgeWords_BothWordsNotInGraph() {
        assertEquals("No bridge words from ha to ha!", graph.queryBridgeWords("ha", "ha"));
    }

    @Test
    public void testQueryBridgeWords_OneWordNotInGraph() {
        assertEquals("No bridge words from ha to a!", graph.queryBridgeWords("ha", "a"));
    }
}
