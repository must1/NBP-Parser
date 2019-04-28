package pl.parser.nbp.conditionchecker;

import java.time.LocalDate;
import java.util.Calendar;

public class ConditionCheckerService implements ConditionChecker {

    private static final int YEAR = 0;

    @Override
    public boolean isDayIncludedInCurrentYear(LocalDate givenDay) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String currentDay = givenDay.toString();
        String[] splittedCurrentDay = currentDay.split("-");
        return Integer.parseInt(splittedCurrentDay[YEAR]) == currentYear;
    }
}
