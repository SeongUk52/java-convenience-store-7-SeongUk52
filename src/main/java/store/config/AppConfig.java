package store.config;

import store.infrastructure.DataParser;
import store.infrastructure.ProductCsvDataParser;
import store.model.Product;

public class AppConfig {
    private static final AppConfig instance = new AppConfig();
    DataParser<Product> productCsvDataParser = new ProductCsvDataParser();

    private AppConfig() {}

    public static AppConfig getInstance() {
        return instance;
    }

    public DataParser<Product> getProductCsvDataParser() {
        return productCsvDataParser;
    }
}
