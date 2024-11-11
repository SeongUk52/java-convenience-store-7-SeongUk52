package store.util;

import static store.constants.ErrorMessage.INVALID_FORMAT;

public class ProductValidator {
    static public void validateProduct(String name, Integer amount) {
        validateName(name);
        validateAmount(amount);
    }

    static private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    static private void validateAmount(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }
}
