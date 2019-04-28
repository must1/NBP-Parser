package pl.parser.nbp.calculations;

import java.util.List;

public class RateCalculations {

    public float getAverageBuyingRate(float sumOfBuyingRate, int numberOfDays) {
        return sumOfBuyingRate / numberOfDays;
    }

    public float getStandardDeviationSellingRate(List<Float> sellingRates) {
        float sumOfSellingRates = 0;
        for (Float sellingRate : sellingRates) {
            sumOfSellingRates += sellingRate;
        }

        float arithmeticAverageOfSellingRates = sumOfSellingRates / sellingRates.size();

        double variance = 0;
        int sellingRatesSize = sellingRates.size();
        for (Float sellingRate : sellingRates) {
            variance += (Math.pow((sellingRate - arithmeticAverageOfSellingRates), 2)) / sellingRatesSize;
        }
        return (float) Math.sqrt(variance);
    }
}
