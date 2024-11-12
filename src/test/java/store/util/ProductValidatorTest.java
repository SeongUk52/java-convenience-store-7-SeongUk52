package store.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductValidatorTest {
    @DisplayName("예외 처리")
    @ParameterizedTest()
    @CsvSource({
            "invalidProduct1, 0",
            "invalidProduct2, -1",
            ",3",
            "eee,",
            ","
    })
    void validateProduct(String productName, Integer quantity) {
        assertThrows(IllegalArgumentException.class, () -> {
            ProductValidator.validateProduct(productName, quantity);
        });
    }
}