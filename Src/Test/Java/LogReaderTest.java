import DataParser.LogReader;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LogReaderTest {

   private LogReader logReader;

    @Test
    public void checkDateBetween() throws FileNotFoundException, ParseException{
        logReader = new LogReader();
        Timestamp lowerBound = new Timestamp(System.currentTimeMillis() -  600000);
        Timestamp upperBound = new Timestamp(System.currentTimeMillis());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis() - 300000);

        boolean valid = logReader.validateTimeStamp(lowerBound,upperBound, currentTime);

        Assert.assertEquals(true,valid);
    }


    @Test
    public void setHexCode() throws FileNotFoundException, ParseException {
        List<String> logData = logReader.getLogData();
        List<Timestamp> logTimeStamps = new ArrayList<>();

        for (String s :
                logData) {
            logTimeStamps.add(logReader.getLogTimeStamp(s));
        }

    }




    @Test
    public void getLogDate() throws FileNotFoundException, ParseException {
        logReader = new LogReader();
       List<String> logData = logReader.getLogData();
       int size = logData.size();


    }

    @Test
    public void getHexCodes() {
    }

    @Test
    public void checkStatus() {
    }

    @Test
    public void getHexStrings() {
    }

    @Test
    public void getLogData() throws FileNotFoundException, ParseException, IOException {
        logReader = new LogReader();
        List<String> logData = logReader.getLogData();

        BufferedReader br = new BufferedReader(new FileReader(LogReader.getProjectPath()));
        String line;
        int rows = 0;
        while((line = br.readLine()) != null)
            rows++;

        Assert.assertEquals(rows,logData.size());

    }



    @Test
    public void getTimeStamp() {
    }

    @Test
    public void getDateBetween1() {
    }



    @Test
    public void getLogTimeStamp() {
    }
}
