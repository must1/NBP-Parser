package pl.parser.nbp;

import pl.parser.nbp.calculations.RateCalculations;
import pl.parser.nbp.contentfetcher.XMLDataFetcher;
import pl.parser.nbp.model.Currency;
import pl.parser.nbp.model.Rates;
import pl.parser.nbp.validation.ConditionChecker;
import pl.parser.nbp.validation.ConditionCheckerService;
import pl.parser.nbp.historysystem.HistorySystem;
import pl.parser.nbp.historysystem.HistorySystemService;
import pl.parser.nbp.view.NbpParserView;

import java.time.LocalDate;

public class MainClass {

    public static void main(String[] args) {
        Rates rates = new Rates();
        NbpParserView nbpParserView = new NbpParserView();
        XMLDataFetcher dataFetcher = new XMLDataFetcher();
        HistorySystem historySystem = new HistorySystemService();
        RateCalculations rateCalculations = new RateCalculations();
        ConditionChecker conditionChecker = new ConditionCheckerService();
        NBPRatesProvider nbpRatesProvider = new NBPRatesProvider(dataFetcher, rates);
        NBPParserEngine nbpParserEngine = new NBPParserEngine(conditionChecker, rateCalculations,
                historySystem, nbpParserView, nbpRatesProvider, rates, dataFetcher);

        String currency = String.valueOf(Currency.forName(args[0]));
        LocalDate startDate = LocalDate.parse(args[1]);
        LocalDate endDate = LocalDate.parse(args[2]);

        nbpParserEngine.executeNbpParserEngine(startDate, endDate, currency);
    }
}
