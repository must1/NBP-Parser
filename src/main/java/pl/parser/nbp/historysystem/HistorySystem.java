package pl.parser.nbp.historysystem;

import java.time.LocalDate;

public interface HistorySystem {

    void overwriteFileWithGivenResult(String currency, float averageBuyingRate, float standardDeviationSellingRate, LocalDate startDateString, LocalDate endDateString, String rankingFilePath);
}
