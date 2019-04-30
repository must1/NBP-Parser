package pl.parser.nbp;

import org.junit.Test;
import pl.parser.nbp.calculations.RateCalculations;
import pl.parser.nbp.conditionchecker.ConditionChecker;
import pl.parser.nbp.conditionchecker.ConditionCheckerService;
import pl.parser.nbp.historysystem.HistorySystemService;
import pl.parser.nbp.view.NbpParserView;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class NBPParserEngineTest {

    private DataFetcher dataFetcher = new DataFetcher();
    private NbpParserView nbpParserView = new NbpParserView();
    private HistorySystemService historySystemService = new HistorySystemService();
    private RateCalculations rateCalculations = new RateCalculations();
    private ConditionChecker conditionChecker = new ConditionCheckerService();

    private NBPParserEngine nbpParserEngine = new NBPParserEngine(conditionChecker, dataFetcher, rateCalculations, historySystemService, nbpParserView);

    @Test
    public void findDaysBetweenFirstAndSecondDate() {

        LocalDate startDate = LocalDate.of(2013, 1, 28);
        LocalDate endDate = LocalDate.of(2013, 1, 31);

        List<LocalDate> actualDays = nbpParserEngine.getDaysBetween(startDate, endDate);
        List<LocalDate> expectedDays = new LinkedList<>();

        expectedDays.add(LocalDate.of(2013, 1, 28));
        expectedDays.add(LocalDate.of(2013, 1, 29));
        expectedDays.add(LocalDate.of(2013, 1, 30));
        expectedDays.add(LocalDate.of(2013, 1, 31));

        assertEquals(actualDays, expectedDays);
    }
}