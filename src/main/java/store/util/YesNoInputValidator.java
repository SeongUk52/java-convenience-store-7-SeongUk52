package store.util;

import static store.constants.ErrorMessage.INVALID_INPUT;

public class YesNoInputValidator {
    public static void validateYesNoInput(String input) {
        if (input == null
                || (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N"))) {
            throw new IllegalArgumentException(INVALID_INPUT.getMessage());
        }
    }
}
