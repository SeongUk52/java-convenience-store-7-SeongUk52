package store.repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import store.infrastructure.DataParser;
import store.infrastructure.FileUtils;
import store.model.Product;
import store.util.ProductValidator;

public class FileProductRepository implements ProductRepository {
    private final List<Product> products;

    public FileProductRepository(String filePath, DataParser<Product> dataParser) throws IOException, URISyntaxException {
        products = FileUtils.loadFromFile(filePath, dataParser);
    }

    @Override
    public List<Product> findByName(String name) {
        return products.stream()
                .filter(product -> product.isNameEqualTo(name))
                .toList();
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    public void saveToFile(String filePath) throws IOException, URISyntaxException {
        FileUtils.saveToFile(filePath, products);
    }
}
