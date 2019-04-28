package pl.parser.nbp.historysystem;

public interface HistorySystem {

    void overwriteFileWithGivenResult(String currency, float averageBuyingRate, float standardDeviationSellingRate, String startDateString, String endDateString, String rankingFilePath);
}
