package it.simonetagliaferri.utils;

import java.time.LocalDate;

public class DateRules {

    public static LocalDate isDateValid(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            return null;
        }
        return date;
    }

    public static boolean isStartDateValid(LocalDate startDate, LocalDate signupDeadline, LocalDate endDate) {
        if (signupDeadline != null) {
            return startDate.isAfter(signupDeadline);
        }
        return isDeadlineValid(startDate, endDate);
    }

    public static boolean isDeadlineValid(LocalDate deadline, LocalDate startDate) {
        if (startDate != null) {
            return deadline.isBefore(startDate);
        }
        return true;
    }

    public static boolean isEndDateValid(LocalDate endDate, LocalDate startDate) {
        return !endDate.isBefore(startDate);
    }

    public static LocalDate minimumStartDate() {
        return LocalDate.now().plusDays(2);
    }

    public static LocalDate minimumStartDate(LocalDate signupDeadline) {
        return signupDeadline.plusDays(1);
    }

    public static LocalDate maxDeadline() {
        return LocalDate.now().plusDays(1);
    }

    public static LocalDate maxDeadline(LocalDate startDate) {
        return startDate.minusDays(1);
    }

    public static LocalDate minimumEndDate(LocalDate startDate) {
        return startDate;
    }

}
