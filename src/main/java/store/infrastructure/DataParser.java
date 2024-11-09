package store.infrastructure;

public interface DataParser<T> {
    T parseLine(String line);
    default boolean isValidLine(String line) {
        return line != null && !line.trim().isEmpty();
    }
}
