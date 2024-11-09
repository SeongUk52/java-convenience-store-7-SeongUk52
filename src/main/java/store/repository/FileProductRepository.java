package store.repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import store.config.AppConfig;
import store.infrastructure.FileUtils;
import store.model.Product;

public class FileProductRepository implements ProductRepository {
    private final List<Product> products;

    public FileProductRepository(String filePath) throws IOException, URISyntaxException {
        AppConfig appConfig = AppConfig.getInstance();
        products = FileUtils.loadFromFile(filePath, appConfig.getProductCsvDataParser());
    }

    @Override
    public List<Product> findByName(String name) {
        return products.stream()
                .filter(product -> product.isNameEqualTo(name))
                .toList();
    }
}
