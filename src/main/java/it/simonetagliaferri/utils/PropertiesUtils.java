package it.simonetagliaferri.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class PropertiesUtils {

    public static final String PERSISTENCE_PROPERTIES = "src/main/resources/properties/persistence.properties";
    public static final String UI_PROPERTIES = "src/main/resources/properties/ui.properties";
    public static final String PERSISTENCE_KEY = "PERSISTENCE_MODE";
    public static final String UI_KEY = "UI_MODE";

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
    public static <T extends Enum<T>> T loadProperty(String filePath, String property, Class<T> enumType) {
        // Trim spaces and set to upper case to avoid false negatives.
        String value = null;
        try {
            value = readProperty(filePath, property).trim().toUpperCase();
            return Enum.valueOf(enumType, value);
        } catch (IOException e) {
            CliUtils.println("Error in reading persistence mode: " + e.getMessage());
            CliUtils.println("Falling back to default value: " + enumType.getEnumConstants()[0].toString());
            return enumType.getEnumConstants()[0];
        } catch (IllegalArgumentException e) {
            CliUtils.println("Invalid value '" + value + "' for property '" + property +
                    "'. Expected one of: " + Arrays.toString(enumType.getEnumConstants()));
            CliUtils.println("Falling back to default value: " + enumType.getEnumConstants()[0].toString());
            return enumType.getEnumConstants()[0];
        }
    }
}
