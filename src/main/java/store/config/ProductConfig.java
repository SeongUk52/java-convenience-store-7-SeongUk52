package store.config;

import store.infrastructure.DataParser;
import store.infrastructure.ProductCsvDataParser;
import store.model.Product;
import store.repository.FileProductRepository;
import store.repository.ProductRepository;

public class ProductConfig {
    private static final ProductConfig instance = new ProductConfig();
    private final ProductRepository productRepository;

    private ProductConfig() {
        try {
            DataParser<Product> productCsvDataParser = new ProductCsvDataParser();
            productRepository = new FileProductRepository("products.md", productCsvDataParser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ProductConfig getInstance() {
        return instance;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }
}
