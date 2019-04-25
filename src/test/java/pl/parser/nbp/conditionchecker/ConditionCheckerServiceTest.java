package pl.parser.nbp.conditionchecker;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.Assert.*;

public class ConditionCheckerServiceTest {

    private ConditionChecker conditionChecker = new ConditionCheckerService();

    @Test
    public void checkIfGivenDayIsIncludedInCurrentYear() {
        LocalDate actualDate = LocalDate.of(2019, 1, 2);
        boolean isIncluded = conditionChecker.checkIfGivenDayIsIncludedInCurrentYear(actualDate);
        assertTrue(isIncluded);
    }
}