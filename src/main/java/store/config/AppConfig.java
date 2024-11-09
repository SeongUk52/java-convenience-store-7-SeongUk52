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
import store.service.ProductService;
import store.service.ProductServiceImpl;
import store.service.PromotionService;
import store.service.PromotionServiceImpl;

public class AppConfig {
    private static final AppConfig instance = new AppConfig();
    private final FileProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionService promotionService;
    private final ProductService productService;

    private AppConfig() {
        try {
            DataParser<Product> productCsvDataParser = new ProductCsvDataParser();
            productRepository = new FileProductRepository("products.md", productCsvDataParser);
            DataParser<Promotion> promotionCsvDataParser = new PromotionCsvDataParser();
            promotionRepository = new FilePromotionRepository("promotions.md", promotionCsvDataParser);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        promotionService = new PromotionServiceImpl(promotionRepository);
        productService = new ProductServiceImpl(productRepository, promotionService);
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

    public ProductService getProductService() {
        return productService;
    }

    public PromotionService getPromotionService() {
        return promotionService;
    }
}
