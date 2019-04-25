package pl.parser.nbp.conditionchecker;

import java.time.LocalDate;

public interface ConditionChecker {

    boolean checkIfGivenDayIsIncludedInCurrentYear(LocalDate givenDay);
}
