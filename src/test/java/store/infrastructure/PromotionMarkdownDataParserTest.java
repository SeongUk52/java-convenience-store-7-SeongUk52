package store.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.Promotion;

class PromotionMarkdownDataParserTest {

    @DisplayName("Promotion 파싱 테스트")
    @Test
    void parseLine() {
        DataParser<Promotion> promotionDataParser = new PromotionMarkdownDataParser();

        Promotion parsed = promotionDataParser.parseLine("탄산2+1,2,1,2024-01-01,2024-12-31");

        assertTrue(parsed.isNameEqualTo("탄산2+1"));
    }
}