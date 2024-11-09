package store.infrastructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FileUtils {
    public static <T> List<T> loadFromFile(String filePath, DataParser<T> parser) throws IOException {
        List<T> items = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        lines.stream()
                .skip(1)
                .filter(parser::isValidLine)
                .map(parser::parseLine)
                .forEach(items::add);

        return items;
    }

    public static <T> void saveToFile(
            String filePath,
            List<T> items,
            Function<T, String> formatter
    ) throws IOException {
        List<String> lines = new ArrayList<>();
        Path path = Paths.get(filePath);
        List<String> fileLines = Files.readAllLines(path);

        if (!fileLines.isEmpty()) {
            lines.add(fileLines.get(0));
        }

        lines.addAll(items.stream()
                .map(formatter)
                .toList());

        Files.write(path, lines);
    }
}
