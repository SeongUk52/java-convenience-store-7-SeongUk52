package store.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.infrastructure.DataParser;
import store.infrastructure.PromotionCsvDataParser;
import store.model.Product;
import store.model.Promotion;
import store.repository.FilePromotionRepository;
import store.repository.PromotionRepository;

class PromotionServiceImplTest {

    @DisplayName("프로모션 기간 체크")
    @Test
    void isPromotionActiveForProduct() throws IOException, URISyntaxException {
        DataParser<Promotion> promotionDataParser = new PromotionCsvDataParser();
        PromotionRepository testPromotionRepository = new FilePromotionRepository(
                "promotions_test.md",
                promotionDataParser
        );
        PromotionService promotionService = new PromotionServiceImpl(testPromotionRepository);

        Product product = new Product("탄산수", 1000, 10, "탄산2+1");

        assertTrue(promotionService.isPromotionActiveForProduct(product));
    }
}