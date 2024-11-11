package store.util;

public class YesNoInputValidator {
    public static void validateYesNoInput(String input) {
        if (input == null
                || (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N"))) {
            throw new IllegalArgumentException("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }
}
