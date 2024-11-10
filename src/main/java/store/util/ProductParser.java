package store.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProductParser {
    public static Map<String, Integer> parse(String input) {
        Pattern pattern = Pattern.compile("\\[(.*?)-(\\d+)\\]");
        Matcher matcher = pattern.matcher(input);

        return matcher.results()
                .collect(Collectors.toMap(
                        match -> match.group(1),
                        match -> Integer.parseInt(match.group(2))
                ));
    }
}
