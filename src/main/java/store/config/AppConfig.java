package store.config;

import java.io.IOException;
import java.net.URISyntaxException;
import store.infrastructure.DataParser;
import store.infrastructure.ProductCsvDataParser;
import store.infrastructure.PromotionCsvDataParser;
import store.model.Product;
import store.model.Promotion;
import store.repository.FileProductRepository;
import store.repository.FilePromotionRepository;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

public class AppConfig {
    private static final AppConfig instance = new AppConfig();
    DataParser<Product> productCsvDataParser = new ProductCsvDataParser();
    DataParser<Promotion> promotionDataParser = new PromotionCsvDataParser();
    FileProductRepository productRepository;
    PromotionRepository promotionRepository;

    private AppConfig() {
        try {
            productRepository = new FileProductRepository("products.md", productCsvDataParser);
            promotionRepository = new FilePromotionRepository("promotions.md", promotionDataParser);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static AppConfig getInstance() {
        return instance;
    }

    public FileProductRepository getProductRepository() {
        return productRepository;
    }

    public PromotionRepository getPromotionRepository() {
        return promotionRepository;
    }
}
