package pl.parser.nbp.calculations;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RateCalculationsTest {

    private RateCalculations rateCalculations = new RateCalculations();

    @Test
    public void getStandardDeviationSellingRate() {
        float expectedStandardDeviation = 1.4907100200653076f;
        List<Float> exampleValues = new ArrayList<>();
        exampleValues.add(1f);
        exampleValues.add(2f);
        exampleValues.add(3f);
        exampleValues.add(4f);
        exampleValues.add(5f);
        exampleValues.add(5f);
        float actualStandardDeviation = rateCalculations.getStandardDeviationSellingRate(exampleValues);

        assertEquals(actualStandardDeviation, expectedStandardDeviation, 0.0002);
    }
}