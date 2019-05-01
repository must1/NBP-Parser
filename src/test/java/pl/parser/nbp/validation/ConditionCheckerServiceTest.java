package pl.parser.nbp.validation;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class ConditionCheckerServiceTest {

    private ConditionChecker conditionChecker = new ConditionCheckerService();

    @Test
    public void checkIfGivenDayIsIncludedInCurrentYear() {
        LocalDate actualDate = LocalDate.of(2019, 1, 2);
        boolean isIncluded = conditionChecker.isDayIncludedInCurrentYear(actualDate);
        assertTrue(isIncluded);
    }
}