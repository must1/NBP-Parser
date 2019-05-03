package pl.parser.nbp.view;

public class NbpParserView {

    public void executeStandardDeviationSellingRateMessage(float standardDeviationSellingRate) {
        System.out.printf("Standard deviation of selling rate: %.4f\n", standardDeviationSellingRate);
    }

    public void executeAverageBuyingRateMessage(float averageBuyingRate) {
        System.out.println("Average buying rate: " + averageBuyingRate);
    }

    public void executeMessageAboutSavingResultInFile() {
        System.out.println("Result was saved in RatesHistory.txt!");
    }
}
