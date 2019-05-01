package pl.parser.nbp.validation;

import java.time.LocalDate;

public interface ConditionChecker {

    boolean isDayIncludedInCurrentYear(LocalDate givenDay);
}
