package store.infrastructure;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.io.IOException;
import java.io.FileNotFoundException;

public class FileUtils {
    public static <T> List<T> loadFromFile(String filePath, DataParser<T> parser) throws IOException, URISyntaxException {
        List<T> items = new ArrayList<>();

        Path path = urlToUri(filePath);

        List<String> lines = Files.readAllLines(path);

        lines.stream()
                .skip(1)
                .filter(parser::isValidLine)
                .map(parser::parseLine)
                .forEach(items::add);

        return items;
    }

    public static <T> void saveToFile(
            String filePath,
            List<T> items
    ) throws IOException, URISyntaxException {
        List<String> lines = new ArrayList<>();
        Path path = Paths.get(filePath);
        List<String> fileLines = Files.readAllLines(path);

        if (!fileLines.isEmpty()) {
            lines.add(fileLines.get(0));
        }

        lines.addAll(items.stream()
                .map(Object::toString)
                .toList());

        Files.write(path, lines);
    }

    private static Path urlToUri(String filePath) throws FileNotFoundException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(filePath);

        if (resource == null) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + filePath);
        }

        return Paths.get(resource.toURI());
    }
}
