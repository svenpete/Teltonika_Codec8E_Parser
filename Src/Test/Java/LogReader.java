
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LogReader {

    private DataParser.LogReader logReader;

    @Test
    public void checkDateBetween() throws FileNotFoundException, ParseException {
        logReader = new DataParser.LogReader();
        Timestamp lowerBound = new Timestamp(System.currentTimeMillis() - 600000);
        Timestamp upperBound = new Timestamp(System.currentTimeMillis());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis() - 300000);

        boolean valid = logReader.validateTimeStamp(lowerBound, upperBound, currentTime);

        Assert.assertEquals(true, valid);
    }


    @Test
    public void testLogData() throws FileNotFoundException, ParseException {

        logReader = new DataParser.LogReader();
        List<String> logData = logReader.getLogData();

        List<Timestamp> logTimeStamps = new ArrayList<>();


        for (String s : logData) {
            logTimeStamps.add(logReader.getLogTimeStamp(s));
        }
        System.out.println();

    }


    @Test
    public void getLogDate() throws FileNotFoundException, ParseException {
        logReader = new DataParser.LogReader();
        List<String> logData = logReader.getLogData();
        int size = logData.size();


    }


    @Test
    public void getLogData() throws FileNotFoundException, ParseException, IOException {
        logReader = new DataParser.LogReader();
        List<String> logData = logReader.getLogData();

        BufferedReader br = new BufferedReader(new FileReader(DataParser.LogReader.getProjectPath()));
        String line;
        int rows = 0;
        while ((line = br.readLine()) != null)
            rows++;

        Assert.assertEquals(rows, logData.size());

    }
}



