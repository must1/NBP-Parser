package pl.parser.nbp;

import pl.parser.nbp.calculations.RateCalculations;
import pl.parser.nbp.conditionchecker.ConditionChecker;
import pl.parser.nbp.historysystem.HistorySystemService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class NBPParserEngine {

    private static final String KOD_WALUTY = "kod_waluty";
    private static final int YEAR = 0;
    private ConditionChecker conditionChecker;
    private DataFetcher dataFetcher;
    private RateCalculations rateCalculations;
    private HistorySystemService historySystemService;
    private float buyingRate;
    private float sellingRate;
    private float sumOfBuyingRate = 0;
    private List<Float> sellingRates = new LinkedList<>();

    NBPParserEngine(ConditionChecker conditionChecker, DataFetcher dataFetcher, RateCalculations rateCalculations, HistorySystemService historySystemService) {
        this.conditionChecker = conditionChecker;
        this.dataFetcher = dataFetcher;
        this.rateCalculations = rateCalculations;
        this.historySystemService = historySystemService;
    }

    void executeNBPParserEngine(String startDateString, String endDateString, String currency) {
        List<LocalDate> daysBetweenFirstAndSecondDate = findDaysBetweenFirstAndSecondDate(startDateString, endDateString);

        for (LocalDate iteratedDay : daysBetweenFirstAndSecondDate) {
            if (conditionChecker.checkIfGivenDayIsIncludedInCurrentYear(iteratedDay))
                try {
                    String DIR_SOURCE = "http://www.nbp.pl/kursy/xml/dir.txt";
                    String line = dataFetcher.findLineWithGivenDate(String.valueOf(iteratedDay), DIR_SOURCE);

                    sumBuyingAndSellingRate(line, currency);

                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            else {
                try {
                    String iteratedDayString = iteratedDay.toString();
                    String[] iteratedStringArray = iteratedDayString.split("-");
                    String DIR_SOURCE = "http://www.nbp.pl/kursy/xml/dir" + iteratedStringArray[YEAR] + ".txt";
                    String line = dataFetcher.findLineWithGivenDate(String.valueOf(iteratedDay), DIR_SOURCE);

                    sumBuyingAndSellingRate(line, currency);

                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }
        float averageBuyingRate = rateCalculations.getAverageBuyingRate(sumOfBuyingRate, sellingRates.size());
        float standardDeviationSellingRate = rateCalculations.getStandardDeviationSellingRate(sellingRates);
        System.out.println("Average buying rate: " + averageBuyingRate);
        System.out.printf("Standard deviation of selling rate: %.4f\n", standardDeviationSellingRate);
        historySystemService.overwriteFileWithGivenResult(averageBuyingRate, standardDeviationSellingRate, startDateString, endDateString, "RatesHistory.txt");
        System.out.println("Result was saved in RatesHistory.txt!");
    }

    private void sumBuyingAndSellingRate(String line, String currency) throws ParserConfigurationException, SAXException, IOException {
        if (line != null) {
            String URL_SOURCE = "http://www.nbp.pl/kursy/xml/" + line + ".xml";
            Document doc = dataFetcher.getXML(URL_SOURCE);
            NodeList nList = doc.getElementsByTagName("pozycja");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getElementsByTagName(KOD_WALUTY).item(0).getTextContent().equals(currency)) {
                        buyingRate = getBuyingRateFromDOM(eElement);
                        sellingRate = getSellingRateFromDOM(eElement);
                    }
                }
            }
            sumOfBuyingRate += buyingRate;
            sellingRates.add(sellingRate);
        }
    }

    private float getSellingRateFromDOM(Element eElement) {
        return Float.parseFloat(eElement
                .getElementsByTagName("kurs_sprzedazy")
                .item(0)
                .getTextContent().replaceAll(",", "."));
    }


    private float getBuyingRateFromDOM(Element eElement) {
        return Float.parseFloat((eElement
                .getElementsByTagName("kurs_kupna")
                .item(0)
                .getTextContent().replaceAll(",", ".")));
    }

    List<LocalDate> findDaysBetweenFirstAndSecondDate(String startDateString, String endDateString) {
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalDate endDate = LocalDate.parse(endDateString);

        Stream<LocalDate> dates = startDate.datesUntil(endDate.plusDays(1));
        return dates.collect(Collectors.toList());
    }
}
