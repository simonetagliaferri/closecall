package unit;

import it.simonetagliaferri.utils.DateRules;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DateRulesTest {

    @Test
    void isStartDateValidReturnsFalseForPastDate() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate signupDeadline = null;
        LocalDate endDate = null;
        assertFalse(DateRules.isStartDateValid(yesterday, signupDeadline, endDate));
    }

    @Test
    void isStartDateValidReturnsTrueForDayAfterTomorrow() {
        LocalDate dayAfterTomorrow = LocalDate.now().plusDays(2);
        LocalDate signupDeadline = null;
        LocalDate endDate = null;
        assertTrue(DateRules.isStartDateValid(dayAfterTomorrow, signupDeadline, endDate));
    }

    @Test
    void isStartDateValidReturnsTrueWhenAfterSignupDeadline() {
        LocalDate signupDeadline = LocalDate.now().plusDays(1);
        LocalDate startDate = signupDeadline.plusDays(2);
        assertTrue(DateRules.isStartDateValid(startDate, signupDeadline, null));
    }

    @Test
    void isStartDateValidReturnsFalseWhenNotAfterSignupDeadline() {
        LocalDate signupDeadline = LocalDate.now();
        assertFalse(DateRules.isStartDateValid(signupDeadline, signupDeadline, null));
    }

    @Test
    void isEndDateValidReturnsFalseWhenEndBeforeStart() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(1);
        assertFalse(DateRules.isEndDateValid(endDate, startDate));
    }
}
