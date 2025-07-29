package it.simonetagliaferri.utils.converters;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    private static final String DATE_FORMAT = "MM/dd/yyyy";

    public static LocalDate formatDate(String date) throws DateTimeException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(date, df);
    }

    public static String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
