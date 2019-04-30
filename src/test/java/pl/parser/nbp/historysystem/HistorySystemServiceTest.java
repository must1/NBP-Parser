package pl.parser.nbp.historysystem;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;

public class HistorySystemServiceTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    private HistorySystemService historySystemService = new HistorySystemService();

    @Test
    public void overwriteFileWithGivenResult() throws IOException {
        File rankingFile = tempFolder.newFile("History.txt");
        String rankingFilePath = rankingFile.getPath();
        LocalDate startDate = LocalDate.of(2013, 1, 28);
        LocalDate endDate = LocalDate.of(2013, 1, 31);


        String[] actualResultBeforeOverwriting = retrieveRatesHistory(rankingFilePath);
        String[] expectedResultBeforeOverwriting = {};

        assertArrayEquals(expectedResultBeforeOverwriting, actualResultBeforeOverwriting);

        historySystemService.overwriteFileWithGivenResult("USD", 4.15f, 0.012f, startDate, endDate, rankingFilePath);

        String[] afterOverwriting = retrieveRatesHistory(rankingFilePath);
        String[] expectedResultAfterOverwriting = {"2013-01-28 till 2013-01-31 for USD", "Average buying rate: 4.15", "Standard deviation of selling rate: 0.012"};

        rankingFile.deleteOnExit();
        assertArrayEquals(expectedResultAfterOverwriting, afterOverwriting);
    }


    private String[] retrieveRatesHistory(String rankingFilePath) {
        try (Stream<String> contentFileStream = Files.lines(Paths.get(rankingFilePath))) {
            return contentFileStream.toArray(String[]::new);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}