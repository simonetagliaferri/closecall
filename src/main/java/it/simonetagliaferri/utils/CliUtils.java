package it.simonetagliaferri.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CliUtils {

    private static final String EOF = "EOF used as input.";

    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private CliUtils() {
    }

    public static String prompt(String label) {
        print(label);
        try {
            String input = reader.readLine();
            if (input == null) {
                println(EOF);
                System.exit(-1);
            }
            return input;
        } catch (IOException e) {
            println("Input error. Try again.");
            return "";
        }
    }

    public static int promptInt(String label) {
        while (true) {
            print(label);
            try {
                String input = reader.readLine();
                if (input == null) {
                    println(EOF);
                    System.exit(-1);
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException | IOException e) {
                println("Please enter a valid number.");
            }
        }
    }

    public static double promptDouble(String label) {
        while (true) {
            print(label);
            try {
                String input = reader.readLine();
                if (input == null) {
                    println(EOF);
                    System.exit(-1);
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException | IOException e) {
                println("Please enter a valid number.");
            }
        }
    }

    public static String multipleChoice(String label, String... choices) {
        int i = 1;
        int result;
        CliUtils.println(label);
        while (true) {
            for (String choice : choices) {
                CliUtils.println(i + ". " + choice);
                i++;
            }
            result = CliUtils.promptInt("Enter choice: ");
            if (result >= 1 && result <= choices.length) {
                return choices[result-1];
            }
            else {
                CliUtils.println("Please enter a valid choice.");
                i=1;
            }
        }
    }

    public static int multipleChoiceInt(String label, String... choices) {
        String choice = multipleChoice(label, choices);
        int i;
        for (i = 0; i <= choices.length; i++) {
            if (choices[i].equals(choice)) {
                break;
            }
        }
        return i+1;
    }

    public static String multipleChoice(String label, List<String> choices) {
        int i = 1;
        int result;
        CliUtils.println(label);
        while (true) {
            for (String choice : choices) {
                CliUtils.println(i + ". " + choice);
                i++;
            }
            result = CliUtils.promptInt("Enter choice: ");
            if (result >= 1 && result <= choices.size()) {
                return choices.get(result-1);
            }
            else {
                CliUtils.println("Please enter a valid choice.");
                i=1;
            }
        }
    }

    public static int multipleChoiceInt(String label, List<String> choices) {
        String choice = multipleChoice(label, choices);
        int i;
        for (i = 0; i < choices.size(); i++) {
            if (choices.get(i).equals(choice)) {
                break;
            }
        }
        return i;
    }

    public static void println(String label) {
        System.out.println(label); //NOSONAR
    }

    public static void print(String label) {
        System.out.print(label); //NOSONAR
    }

}
