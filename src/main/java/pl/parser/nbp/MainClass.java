package pl.parser.nbp;

import pl.parser.nbp.calculations.RateCalculations;
import pl.parser.nbp.conditionchecker.ConditionChecker;
import pl.parser.nbp.conditionchecker.ConditionCheckerService;
import pl.parser.nbp.historysystem.HistorySystemService;

public class MainClass {

    public static void main(String[] args) {

        DataFetcher dataFetcher = new DataFetcher();
        HistorySystemService historySystemService = new HistorySystemService();
        RateCalculations rateCalculations = new RateCalculations();
        ConditionChecker conditionChecker = new ConditionCheckerService();
        NBPParserEngine nbpParserEngine = new NBPParserEngine(conditionChecker, dataFetcher, rateCalculations, historySystemService);

        String currency = args[0];
        String startDateString = args[1];
        String endDateString = args[2];

        nbpParserEngine.executeNBPParserEngine(startDateString, endDateString, currency);
    }
}
