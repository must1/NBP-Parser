package pl.parser.nbp;

import org.junit.Test;
import pl.parser.nbp.calculations.RateCalculations;
import pl.parser.nbp.conditionchecker.ConditionChecker;
import pl.parser.nbp.conditionchecker.ConditionCheckerService;
import pl.parser.nbp.historysystem.HistorySystemService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class NBPParserEngineTest {

    private DataFetcher dataFetcher = new DataFetcher();
    private HistorySystemService historySystemService = new HistorySystemService();
    private RateCalculations rateCalculations = new RateCalculations();
    private ConditionChecker conditionChecker = new ConditionCheckerService();

    private NBPParserEngine nbpParserEngine = new NBPParserEngine(conditionChecker, dataFetcher, rateCalculations, historySystemService);

    @Test
    public void findDaysBetweenFirstAndSecondDate() {

        List<LocalDate> acutalDays = nbpParserEngine.findDaysBetweenFirstAndSecondDate("2013-01-28", "2013-01-31");
        List<LocalDate> expectedDays = new LinkedList<>();

        expectedDays.add(LocalDate.of(2013, 1, 28));
        expectedDays.add(LocalDate.of(2013, 1, 29));
        expectedDays.add(LocalDate.of(2013, 1, 30));
        expectedDays.add(LocalDate.of(2013, 1, 31));

        assertEquals(acutalDays, expectedDays);
    }
}