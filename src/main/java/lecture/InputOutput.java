package lecture;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class InputOutput {
    public static void main(final String[] args) {
        Path path = Paths.get("ausgabe.txt");
        List<String> strings = Arrays.asList("Hello", "World");

        try {
            Files.write(path, strings);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            List<String> result = Files.readAllLines(path, StandardCharsets.UTF_8);
            System.out.println(result);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] readLine(final Path path)
            throws IOException {
        try (BufferedReader reader
                     = Files.newBufferedReader(path)) {
            String[] result = new String[0];
            for (;;) {
                String line = reader.readLine();
                if (line == null) { break; }
                result = ArrayUtils.add(result, line);
            }
            return result;
        }
    }

    public void writeLines(Path path, String... lines)
            throws IOException {
        try (BufferedWriter writer
                     = Files.newBufferedWriter(path)) {
            for (String line: lines) {
                writer.append(line);
                writer.newLine();
            }
        }
    }

    public String[] readLines(final Path path)
            throws IOException {
        try (BufferedReader reader
                     = Files.newBufferedReader(path)) {
            String[] result = new String[0];
            for (;;) {
                String line = reader.readLine();
                if (line == null) { break; }
                result = ArrayUtils.add(result, line);
            }
            return result;
        }
    }
}
