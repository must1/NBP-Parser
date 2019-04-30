package pl.parser.nbp.conditionchecker;

import java.time.LocalDate;
import java.util.Calendar;

public class ConditionCheckerService implements ConditionChecker {

    @Override
    public boolean isDayIncludedInCurrentYear(LocalDate givenDay) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return currentYear == givenDay.getYear();
    }
}
