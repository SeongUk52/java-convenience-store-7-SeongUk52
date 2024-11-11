package store.util;

public class ProductValidator {
    static public void validateProduct(String name, int amount) {
        validateAmount(amount);
    }

    static private void validateAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }
}
