package store.config;

import java.io.IOException;
import java.net.URISyntaxException;
import store.infrastructure.DataParser;
import store.infrastructure.ProductCsvDataParser;
import store.model.Product;
import store.repository.FileProductRepository;
import store.repository.ProductRepository;

public class AppConfig {
    private static final AppConfig instance = new AppConfig();
    DataParser<Product> productCsvDataParser = new ProductCsvDataParser();
    ProductRepository productRepository;

    private AppConfig() {
        try {
            productRepository = new FileProductRepository("products.md", productCsvDataParser);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static AppConfig getInstance() {
        return instance;
    }

    public DataParser<Product> getProductCsvDataParser() {
        return productCsvDataParser;
    }
}
