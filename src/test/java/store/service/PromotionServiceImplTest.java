package store.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.infrastructure.DataParser;
import store.infrastructure.PromotionMarkdownDataParser;
import store.model.Product;
import store.model.Promotion;
import store.repository.FilePromotionRepository;
import store.repository.PromotionRepository;

class PromotionServiceImplTest {

    @DisplayName("프로모션 기간 체크")
    @Test
    void isPromotionActiveForProduct() throws IOException, URISyntaxException {
        DataParser<Promotion> promotionDataParser = new PromotionMarkdownDataParser();
        PromotionRepository promotionRepository = new FilePromotionRepository(
                "promotions_test.md",
                promotionDataParser
        );
        PromotionService promotionService = new PromotionServiceImpl(promotionRepository);

        Product product = new Product("탄산수", 1000, 10, "탄산2+1");

        assertTrue(promotionService.isPromotionActiveForProduct(
                product,
                LocalDate.of(2024, 11, 10)
        ));
    }
}