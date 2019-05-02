package pl.parser.nbp.contentfetcher;

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
import java.security.ProviderException;
import java.time.LocalDate;

public class XMLDataFetcher {

    public Document getXML(String givenURL) throws IOException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            URLConnection con = openConnection(givenURL);
            try (InputStream is = con.getInputStream()) {
                return dBuilder.parse(is);
            }
        } catch (ParserConfigurationException | SAXException e) {
            throw new ProviderException("Error with file processing!");
        }
    }

    public String findLineWithGivenDate(LocalDate givenDate, String givenURL) throws IOException {
        BufferedReader bufferedReader = getBufferedReader(givenURL);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("c") && line.endsWith(String.valueOf(givenDate.getMonthValue()) + String.valueOf(givenDate.getDayOfMonth()))) {
                break;
            }
        }
        return line;
    }

    private URLConnection openConnection(String givenURL) throws IOException {
        URL url = new URL(givenURL);
        return url.openConnection();
    }

    private BufferedReader getBufferedReader(String givenURL) throws IOException {
        return new BufferedReader(new InputStreamReader(createInputStreamToRead(givenURL)));
    }

    private InputStream createInputStreamToRead(String givenURL) throws IOException {
        URLConnection connection = openConnection(givenURL);
        return connection.getInputStream();
    }
}
