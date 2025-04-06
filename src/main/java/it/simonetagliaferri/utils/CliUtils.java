package it.simonetagliaferri.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CliUtils {

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private CliUtils() {
    }

    public static String prompt(String label) {
        System.out.print(label + ": ");
        try {
            String input = reader.readLine();
            if (input == null) {
                println("EOF used as input.");
                System.exit(-1);
            }
            return input;
        } catch (IOException e) {
            System.out.println("Input error. Try again.");
            return "";
        }
    }

    public static int promptInt(String label) {
        while (true) {
            System.out.print(label + ": ");
            try {
                String input = reader.readLine();
                if (input==null) {
                    println("EOF used as input.");
                    System.exit(-1);
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException | IOException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static void println(String label) {
        System.out.println(label);
    }

}
