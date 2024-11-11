package store.util;

import static store.constants.ErrorMessage.INVALID_FORMAT;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProductParser {
    public static Map<String, Integer> parse(String input) {
        Pattern pattern = Pattern.compile("\\[([\\w가-힣]+)-(\\d+)\\]");
        Matcher matcher = pattern.matcher(input);

        if (!input.matches("^(\\[[\\w가-힣]+-\\d+\\])(,\\[[\\w가-힣]+-\\d+\\])*$")) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }

        return matcher.results()
                .collect(Collectors.toMap(
                        match -> match.group(1),
                        match -> parseNumber(match.group(2))
                ));
    }

    private static int parseNumber(String numberStr) {
        try {
            return Integer.parseInt(numberStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }
}
