package store.config;

import store.repository.FileProductRepository;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.PriceCalculatorService;
import store.service.PriceCalculatorServiceImpl;
import store.service.ProductService;
import store.service.ProductServiceImpl;
import store.service.PromotionService;
import store.service.PromotionServiceImpl;

public class ServiceConfig {
    private static final ServiceConfig instance = new ServiceConfig();

    private ProductService productService;
    private PromotionService promotionService;
    private PriceCalculatorService priceCalculatorService;

    private ServiceConfig() {}

    public static ServiceConfig getInstance() {
        return instance;
    }

    public ProductService getProductService() {
        if (productService == null) {
            ProductRepository productRepository = ProductConfig.getInstance().getProductRepository();
            promotionService = getPromotionService();
            priceCalculatorService = getPriceCalculatorService();
            productService = new ProductServiceImpl((FileProductRepository) productRepository,
                    promotionService, priceCalculatorService);
        }
        return productService;
    }

    public PromotionService getPromotionService() {
        if (promotionService == null) {
            PromotionRepository promotionRepository = PromotionConfig.getInstance().getPromotionRepository();
            promotionService = new PromotionServiceImpl(promotionRepository);
        }
        return promotionService;
    }

    public PriceCalculatorService getPriceCalculatorService() {
        if (priceCalculatorService == null) {
            priceCalculatorService = new PriceCalculatorServiceImpl();
        }
        return priceCalculatorService;
    }
}