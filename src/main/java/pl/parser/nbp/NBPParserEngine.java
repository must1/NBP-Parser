package pl.parser.nbp;

import pl.parser.nbp.calculations.RateCalculations;
import pl.parser.nbp.contentfetcher.XMLDataFetcher;
import pl.parser.nbp.model.Rates;
import pl.parser.nbp.validation.ConditionChecker;
import pl.parser.nbp.historysystem.HistorySystem;
import pl.parser.nbp.view.NbpParserView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class NBPParserEngine {

    private static final String NBP_URL_FOR_CURRENT_YEAR = "http://www.nbp.pl/kursy/xml/dir.txt";
    private static final String HISTORY_FILE = "RatesHistory.txt";
    private static final Logger LOGGER = Logger.getLogger(NBPParserEngine.class.getName());
    private ConditionChecker conditionChecker;
    private RateCalculations rateCalculations;
    private HistorySystem historySystem;
    private NbpParserView nbpParserView;
    private NBPRatesProvider nbpRatesProvider;
    private Rates rates;
    private XMLDataFetcher dataFetcher;

    NBPParserEngine(ConditionChecker conditionChecker, RateCalculations rateCalculations, HistorySystem historySystem, NbpParserView nbpParserView, NBPRatesProvider nbpRatesProvider, Rates rates, XMLDataFetcher dataFetcher) {
        this.conditionChecker = conditionChecker;
        this.rateCalculations = rateCalculations;
        this.historySystem = historySystem;
        this.nbpParserView = nbpParserView;
        this.nbpRatesProvider = nbpRatesProvider;
        this.rates = rates;
        this.dataFetcher = dataFetcher;
    }

    void executeNbpParserEngine(LocalDate startDate, LocalDate endDate, String currency) {

        List<LocalDate> daysBetweenFirstAndSecondDate = getDaysBetween(startDate, endDate);

        for (LocalDate iteratedDay : daysBetweenFirstAndSecondDate) {
            if (conditionChecker.isDayIncludedInCurrentYear(iteratedDay))
                try {
                    String line = dataFetcher.findLineWithGivenDate(iteratedDay, NBP_URL_FOR_CURRENT_YEAR);
                    rates = nbpRatesProvider.getRates(line, currency);

                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "IOException was thrown!", e);
                    System.exit(1);
                }
            else {
                try {
                    String dirSource = "http://www.nbp.pl/kursy/xml/dir" + iteratedDay.getYear() + ".txt";
                    String line = dataFetcher.findLineWithGivenDate(iteratedDay, dirSource);

                    rates = nbpRatesProvider.getRates(line, currency);

                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "IOException was thrown!", e);
                    System.exit(1);
                }
            }
        }

        float averageBuyingRate = rateCalculations.getAverageBuyingRate(rates.getBuyingRates());
        float standardDeviationSellingRate = rateCalculations.getStandardDeviationSellingRate(rates.getSellingRates());
        nbpParserView.getAverageBuyingRateMessage(averageBuyingRate);
        nbpParserView.getStandardDeviationSellingRateMessage(standardDeviationSellingRate);
        historySystem.overwriteFileWithGivenResult(currency, averageBuyingRate, standardDeviationSellingRate, startDate, endDate, HISTORY_FILE);
        nbpParserView.getMessageAboutSavingResultInFile();
    }

    List<LocalDate> getDaysBetween(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
    }
}
