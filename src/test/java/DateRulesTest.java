import it.simonetagliaferri.utils.DateRules;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DateRulesTest {

    @Test
    public void isDateValidReturnsFalseForPastDate() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        assertFalse(DateRules.isDateValid(yesterday));
    }

    @Test
    public void isDateValidReturnsTrueForToday() {
        LocalDate today = LocalDate.now();
        assertTrue(DateRules.isDateValid(today));
    }

    @Test
    public void isStartDateValidReturnsTrueWhenAfterSignupDeadline() {
        LocalDate signupDeadline = LocalDate.now();
        LocalDate startDate = signupDeadline.plusDays(1);
        assertTrue(DateRules.isStartDateValid(startDate, signupDeadline, null));
    }

    @Test
    public void isStartDateValidReturnsFalseWhenNotAfterSignupDeadline() {
        LocalDate signupDeadline = LocalDate.now();
        assertFalse(DateRules.isStartDateValid(signupDeadline, signupDeadline, null));
    }

    @Test
    public void isEndDateValidReturnsFalseWhenEndBeforeStart() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(1);
        assertFalse(DateRules.isEndDateValid(endDate, startDate));
    }
}
