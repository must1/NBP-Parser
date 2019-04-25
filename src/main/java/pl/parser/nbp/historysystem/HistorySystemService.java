package pl.parser.nbp.historysystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HistorySystemService {

    public void overwriteFileWithGivenResult(float averageBuyingRate, float standardDeviationSellingRate, String startDateString, String endDateString, String rankingFilePath) {

        try (FileWriter writer = new FileWriter(rankingFilePath, true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(startDateString + " till " + endDateString);
            bufferedWriter.newLine();
            bufferedWriter.write("Average buying rate: " + averageBuyingRate);
            bufferedWriter.newLine();
            bufferedWriter.write("Standard deviation of selling rate: " + standardDeviationSellingRate);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
