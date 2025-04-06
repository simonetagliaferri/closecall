package it.simonetagliaferri.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class PropertiesUtils {

    private PropertiesUtils() {
    }

    public static String readProperty(String fileName, String property) throws IOException {
        // Using try with resources so that after the try block the BufferedReader is closed.
        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty(property);
        }
    }

    // Used generics to make it work with any kind of ENUM.
    public static <T extends Enum<T>> T loadProperty(String filePath, String property, Class<T> enumType) throws IOException {
        // Trim spaces and set to upper case to avoid false negatives.
        String value = readProperty(filePath, property).trim().toUpperCase();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Empty property: " + property);
        }
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid value '" + value + "' for property '" + property +
                    "'. Expected one of: " + Arrays.toString(enumType.getEnumConstants()));
        }
    }
}
