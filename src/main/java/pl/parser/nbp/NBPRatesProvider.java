package pl.parser.nbp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pl.parser.nbp.contentfetcher.XMLDataFetcher;
import pl.parser.nbp.model.Rates;

import java.io.IOException;

class NBPRatesProvider {

    private static final String CURRENCY_CODE = "kod_waluty";
    private static final String BUY_RATE_TAG = "kurs_kupna";
    private static final String SELL_RATE_TAG = "kurs_sprzedazy";
    private static final String TAG_NAME = "pozycja";
    private static final String URL_SOURCE = "http://www.nbp.pl/kursy/xml/%s.xml";
    private Rates rates;
    private XMLDataFetcher dataFetcher;

    NBPRatesProvider(XMLDataFetcher dataFetcher, Rates rates) {
        this.dataFetcher = dataFetcher;
        this.rates = rates;
    }

    Rates getRates(String line, String currency) throws IOException {
        float buyingRate = 0;
        float sellingRate = 0;

        if (line != null) {
            String urlSource = String.format(URL_SOURCE, line);
            Document doc = dataFetcher.getXML(urlSource);
            NodeList nList = doc.getElementsByTagName(TAG_NAME);

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
            rates.getBuyingRates().add(buyingRate);
            rates.getSellingRates().add(sellingRate);

        }
        return rates;
    }

    private float getFieldFromDOM(Element eElement, String tag) {
        return Float.parseFloat((eElement
                .getElementsByTagName(tag)
                .item(0)
                .getTextContent().replaceAll(",", ".")));
    }
}
