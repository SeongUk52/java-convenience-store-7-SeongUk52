package store.infrastructure;

import java.util.List;
import store.model.Product;

public class ProductMarkdownDataParser implements DataParser<Product> {
    @Override
    public Product parseLine(String line) {
        List<String> fields = List.of(line.split(","));
        return new Product(
                fields.get(0),
                Integer.parseInt(fields.get(1)),
                Integer.parseInt(fields.get(2)),
                fields.get(3)
        );
    }
}
