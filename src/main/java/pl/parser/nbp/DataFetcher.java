package pl.parser.nbp;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class DataFetcher {

    private static final int MONTH = 1;
    private static final int DAY = 2;

    private URLConnection openConnection(String givenURL) throws IOException {
        URL url = new URL(givenURL);
        return url.openConnection();
    }

    private BufferedReader getBufferedReader(String givenURL) throws IOException {
        return new BufferedReader(new InputStreamReader(createInputStreamToRead(givenURL)));
    }

    String findLineWithGivenDate(String givenDate, String givenURL) throws IOException {
        BufferedReader bufferedReader = getBufferedReader(givenURL);
        String[] splittedDay = givenDate.split("-");
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("c") && line.endsWith(splittedDay[MONTH] + splittedDay[DAY])) {
                break;
            }
        }
        return line;
    }

    private InputStream createInputStreamToRead(String givenURL) throws IOException {
        URLConnection connection = openConnection(givenURL);
        return connection.getInputStream();
    }

    Document getXML(String givenURL) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        URLConnection con = openConnection(givenURL);
        try (InputStream is = con.getInputStream()) {
            return dBuilder.parse(is);
        }
    }
}
