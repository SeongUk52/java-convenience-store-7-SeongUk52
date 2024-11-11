package store.util;

public class ProductValidator {
    static public void validateProduct(String name, Integer amount) {
        validateName(name);
        validateAmount(amount);
    }

    static private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    static private void validateAmount(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }
}
