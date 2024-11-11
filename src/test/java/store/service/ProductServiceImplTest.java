package store.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.infrastructure.DataParser;
import store.infrastructure.ProductCsvDataParser;
import store.infrastructure.PromotionCsvDataParser;
import store.model.Product;
import store.model.Promotion;
import store.repository.FileProductRepository;
import store.repository.FilePromotionRepository;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.util.ProductParser;

class ProductServiceImplTest {
    private ProductService productService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        DataParser<Promotion> promotionDataParser = new PromotionCsvDataParser();
        PromotionRepository promotionRepository = new FilePromotionRepository(
                "promotions_test.md",
                promotionDataParser
        );
        DataParser<Product> productDataParser = new ProductCsvDataParser();
        productRepository = new FileProductRepository(
                "products_test.md",
                productDataParser
        );
        PromotionService promotionService = new PromotionServiceImpl(promotionRepository);
        PriceCalculatorService priceCalculatorService = new PriceCalculatorServiceImpl();
        productService = new ProductServiceImpl(
                (FileProductRepository) productRepository,
                promotionService,
                priceCalculatorService
        );
    }

    @DisplayName("상품 구매")
    @Test
    void purchaseProducts() {
        productService.purchaseProducts(ProductParser.parse("[콜라-3]"), true);

        assertEquals(
                7,
                productRepository
                .findByName("콜라")
                .stream()
                .filter(Product::hasPromotion)
                .findFirst()
                .get()
                .getQuantity()
        );
    }

    @DisplayName("증정품이 있을 경우")
    @Test
    void availablePromotionCount() {
        int availablePromotionCount = productService.getAvailablePromotionCount("오렌지주스", 1);

        assertEquals(
                1,
                availablePromotionCount
        );
    }


    @DisplayName("정가 구매")
    @Test
    void insufficientPromotionCount() {
        int insufficientPromotionCount = productService.getInsufficientPromotionCount("감자칩", 10);

        assertEquals(
                6,
                insufficientPromotionCount
        );
    }
}