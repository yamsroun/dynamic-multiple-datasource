package yamsroun.multids.config.datasource.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NamingUtils {

    public static String toLowerCamelCase(String... str) {
        String temp = Stream.of(str)
            .map(NamingUtils::capitalize)
            .collect(Collectors.joining());
        return decapitalize(temp);
    }

    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String decapitalize(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
}
