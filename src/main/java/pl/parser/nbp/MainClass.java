package pl.parser.nbp;

import pl.parser.nbp.calculations.RateCalculations;
import pl.parser.nbp.conditionchecker.ConditionChecker;
import pl.parser.nbp.conditionchecker.ConditionCheckerService;
import pl.parser.nbp.historysystem.HistorySystem;
import pl.parser.nbp.historysystem.HistorySystemService;
import pl.parser.nbp.view.NbpParserView;

import java.time.LocalDate;

public class MainClass {

    public static void main(String[] args) {

        NbpParserView nbpParserView = new NbpParserView();
        DataFetcher dataFetcher = new DataFetcher();
        HistorySystem historySystem = new HistorySystemService();
        RateCalculations rateCalculations = new RateCalculations();
        ConditionChecker conditionChecker = new ConditionCheckerService();
        NBPParserEngine nbpParserEngine = new NBPParserEngine(conditionChecker, dataFetcher, rateCalculations,
                historySystem, nbpParserView);

        String currency = args[0];
        LocalDate startDate = LocalDate.parse(args[1]);
        LocalDate endDate = LocalDate.parse(args[2]);

        nbpParserEngine.executeNbpParserEngine(startDate, endDate, currency);
    }
}
