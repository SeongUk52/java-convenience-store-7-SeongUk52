package store.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.Product;

class ProductCsvDataParserTest {

    @DisplayName("Product파싱 테스트")
    @Test
    void parseLine() {
        DataParser<Product> productDataParser = new ProductCsvDataParser();

        Product parsed = productDataParser.parseLine("감자칩,1500,5,null");

        assertEquals("감자칩", parsed.getName());
        assertEquals("null", parsed.getPromotion());
    }
}