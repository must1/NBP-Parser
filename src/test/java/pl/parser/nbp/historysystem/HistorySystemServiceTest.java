package pl.parser.nbp.historysystem;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class HistorySystemServiceTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    private HistorySystemService historySystemService = new HistorySystemService();

    @Test
    public void overwriteFileWithGivenResult() throws IOException {
        File rankingFile = tempFolder.newFile("History.txt");
        String rankingFilePath = rankingFile.getPath();

        String[] actualResultBeforeOverwriting = historySystemService.retrieveRatesHistory(rankingFilePath);
        String[] expectedResultBeforeOverwriting = {};

        assertArrayEquals(expectedResultBeforeOverwriting, actualResultBeforeOverwriting);

        historySystemService.overwriteFileWithGivenResult(4.15f, 0.012f, "2013-01-28", "2013-01-31", rankingFilePath);

        String[] afterOverwriting = historySystemService.retrieveRatesHistory(rankingFilePath);
        String[] expectedResultAfterOverwriting = {"2013-01-28 till 2013-01-31", "Average buying rate: 4.15", "Standard deviation of selling rate: 0.012"};

        rankingFile.deleteOnExit();
        assertArrayEquals(expectedResultAfterOverwriting, afterOverwriting);
    }
}