package pl.parser.nbp;

import pl.parser.nbp.calculations.RateCalculations;
import pl.parser.nbp.conditionchecker.ConditionChecker;
import pl.parser.nbp.historysystem.HistorySystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pl.parser.nbp.view.NbpParserView;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class NBPParserEngine {

    private static final String CURRENCY_CODE = "kod_waluty";
    private static final String BUY_RATE_TAG = "kurs_kupna";
    private static final String SELL_RATE_TAG = "kurs_sprzedazy";
    private static final String NBP_URL_FOR_CURRENT_YEAR = "http://www.nbp.pl/kursy/xml/dir.txt";
    private static final String HISTORY_FILE = "RatesHistory.txt";
    private ConditionChecker conditionChecker;
    private DataFetcher dataFetcher;
    private RateCalculations rateCalculations;
    private HistorySystem historySystem;
    private NbpParserView nbpParserView;
    private float buyingRate;
    private float sellingRate;
    private List<Float> sellingRates = new LinkedList<>();
    private List<Float> buyingRates = new LinkedList<>();
    private static final Logger LOGGER = Logger.getLogger(NBPParserEngine.class.getName());


    NBPParserEngine(ConditionChecker conditionChecker, DataFetcher dataFetcher, RateCalculations rateCalculations, HistorySystem historySystem, NbpParserView nbpParserView) {
        this.conditionChecker = conditionChecker;
        this.dataFetcher = dataFetcher;
        this.rateCalculations = rateCalculations;
        this.historySystem = historySystem;
        this.nbpParserView = nbpParserView;
    }

    void executeNbpParserEngine(LocalDate startDateString, LocalDate endDateString, String currency) {
        List<LocalDate> daysBetweenFirstAndSecondDate = getDaysBetween(startDateString, endDateString);

        for (LocalDate iteratedDay : daysBetweenFirstAndSecondDate) {
            if (conditionChecker.isDayIncludedInCurrentYear(iteratedDay))
                try {
                    String line = dataFetcher.findLineWithGivenDate(iteratedDay, NBP_URL_FOR_CURRENT_YEAR);
                    fetchBuyingAndSellingRate(line, currency);

                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            else {
                try {
                    String DIR_SOURCE = "http://www.nbp.pl/kursy/xml/dir" + iteratedDay.getYear() + ".txt";
                    String line = dataFetcher.findLineWithGivenDate(iteratedDay, DIR_SOURCE);

                    fetchBuyingAndSellingRate(line, currency);

                } catch (IOException | SAXException | ParserConfigurationException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            }
        }

        float averageBuyingRate = rateCalculations.getAverageBuyingRate(buyingRates);
        float standardDeviationSellingRate = rateCalculations.getStandardDeviationSellingRate(sellingRates);
        nbpParserView.getAverageBuyingRateMessage(averageBuyingRate);
        nbpParserView.getStandardDeviationSellingRateMessage(standardDeviationSellingRate);
        historySystem.overwriteFileWithGivenResult(currency, averageBuyingRate, standardDeviationSellingRate, startDateString, endDateString, HISTORY_FILE);
        nbpParserView.getMessageAboutSavingResultInFile();
    }

    List<LocalDate> getDaysBetween(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
    }

    private void fetchBuyingAndSellingRate(String line, String currency) throws ParserConfigurationException, SAXException, IOException {
        if (line == null) {
            return;
        }
        String URL_SOURCE = "http://www.nbp.pl/kursy/xml/" + line + ".xml";
        Document doc = dataFetcher.getXML(URL_SOURCE);
        NodeList nList = doc.getElementsByTagName("pozycja");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (eElement.getElementsByTagName(CURRENCY_CODE).item(0).getTextContent().equals(currency)) {
                    buyingRate = getFieldFromDOM(eElement, BUY_RATE_TAG);
                    sellingRate = getFieldFromDOM(eElement, SELL_RATE_TAG);
                }
            }
        }
        System.out.println(URL_SOURCE);
        buyingRates.add(buyingRate);
        sellingRates.add(sellingRate);
    }

    private float getFieldFromDOM(Element eElement, String tag) {
        return Float.parseFloat((eElement
                .getElementsByTagName(tag)
                .item(0)
                .getTextContent().replaceAll(",", ".")));
    }
}
