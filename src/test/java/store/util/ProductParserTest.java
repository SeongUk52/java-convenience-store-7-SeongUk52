package store.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductParserTest {

    @DisplayName("사용자 입력 상품 문자열 예외처리")
    @ParameterizedTest()
    @ValueSource(strings = {"[컵라면-컵]", "]컵라면-3]", "컵라면-3", "[컵라면3]", "[컵라면 3]"})
    void parse(String input) {
        assertThrows(IllegalArgumentException.class, () -> ProductParser.parse(input));
    }
}