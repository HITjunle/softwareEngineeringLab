// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



public class Main {
    public static void main(String[] args) {

        Path currentDir = Paths.get(System.getProperty("user.dir"));
        String filePath = currentDir.resolve("test/test1.txt").toString();
        List<String> words = readFile(filePath);
        String[] wordsArray = words.toArray(new String[0]);

        // 打印结果
        for (String word : wordsArray) {
            System.out.println(word);
        }
    }

    private static List<String> readFile(String filePath) {
        List<String> words = new ArrayList<>();
        Pattern pattern = Pattern.compile("[^a-zA-Z]");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                line = pattern.matcher(line).replaceAll(" ").toLowerCase();
                sb.append(line).append(" ");
            }

            String[] wordsArray = sb.toString().trim().split("\\s+");
            for (String word : wordsArray) {
                words.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return words;
    }
}