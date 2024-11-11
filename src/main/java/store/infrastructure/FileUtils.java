package store.infrastructure;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static <T> List<T> loadFromFile(String filePath, DataParser<T> parser) throws IOException, URISyntaxException {
        Path path = urlToUri(filePath);
        List<String> lines = readLinesFromFile(path);

        return parseLines(lines, parser);
    }

    public static <T> void saveToFile(String filePath, List<T> items) throws IOException {
        Path path = Paths.get(filePath);
        List<String> fileLines = readLinesFromFile(path);

        List<String> linesToSave = createLinesToSave(fileLines, items);
        writeLinesToFile(path, linesToSave);
    }

    private static Path urlToUri(String filePath) throws FileNotFoundException, URISyntaxException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(filePath);
        if (resource == null) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + filePath);
        }
        return Paths.get(resource.toURI());
    }

    private static List<String> readLinesFromFile(Path path) throws IOException {
        return Files.readAllLines(path);
    }

    private static <T> List<T> parseLines(List<String> lines, DataParser<T> parser) {
        List<T> items = new ArrayList<>();
        lines.stream()
            .skip(1)
            .filter(parser::isValidLine)
            .map(parser::parseLine)
            .forEach(items::add);
        return items;
    }

    private static <T> List<String> createLinesToSave(List<String> fileLines, List<T> items) {
        List<String> lines = new ArrayList<>();
        if (!fileLines.isEmpty()) {
            lines.add(fileLines.get(0));
        }
        lines.addAll(items.stream()
            .map(Object::toString)
            .toList());
        return lines;
    }

    private static void writeLinesToFile(Path path, List<String> lines) throws IOException {
        Files.write(path, lines);
    }
}