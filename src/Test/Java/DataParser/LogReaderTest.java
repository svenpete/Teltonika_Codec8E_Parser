package DataParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;



public class LogReaderTest {
    private LogReader logReader;
    private List<String> logData;

    @Before
    public void setUp() throws Exception {
        logReader = new DataParser.LogReader();
        logData = logReader.getLogData();
    }

    @Test
    public void testCheckStatus(){
        String toCheck = "2020-08-20 17:18:23,247 INFO   20.08.2020 17:18:23 - received [00000001]";
        boolean valid = logReader.checkStatus(toCheck);
        Assert.assertEquals(true,valid);
    }

    @Test
    public void checkDateBetween() {

        Timestamp lowerBound = new Timestamp(System.currentTimeMillis() - 600000);
        Timestamp upperBound = new Timestamp(System.currentTimeMillis());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis() - 300000);

        boolean valid = logReader.checkTimeStamp(lowerBound, upperBound, currentTime);

        Assert.assertEquals(true, valid);
    }

    @Test
    public void testGetHexData(){
        List<String> hexData = new ArrayList<>();

        for (int i = 0; i < logData.size(); i++) {

            String data = logData.get(i);
            if ( logReader.checkHexDataLength(data) )
             hexData.add(logReader.getHexData(data));

        }
    }


    @Test
    public void testHexLength(){
        List<String> valid = new ArrayList<>();
        List<String> notValid = new ArrayList<>();

        for (String s :
                logData) {
            if (logReader.checkHexDataLength(s)) valid.add(s);
            else notValid.add(s);
        }

        // check if there is a string to
        for (String s :
                notValid) {
            if ( logReader.checkHexDataLength(s) ) Assert.fail("String to long");
        }

    }


    @Test
    public void testFormat(){
        String nonHex = "Diest ist ein Test";
        boolean valid = logReader.checkHexFormat(nonHex);
        Assert.assertEquals(false,valid);
    }


    @Test
    public void testConvertToTimestamp(){
        List<Timestamp> test = new ArrayList<>();
        for (int i = 0; i < logData.size(); i++) {
            try {
                test.add(logReader.convertToTimeStamp(logData.get(i)));
            } catch (ParseException e) {
                e.printStackTrace();
                Assert.fail("Unable to parse Data");
            }
        }
        Assert.assertTrue("No exception thrown", true);

    }

    @Test
    public void testGetLogTimeStamp(){
        List<Timestamp> logTimeStamp = new ArrayList<>();
        try {
            for (int i = 0; i < logData.size(); i++) {
                String data = logData.get(i);
                logTimeStamp.add(logReader.getLogTimeStamp(data));

            }

        }catch (ParseException e){
            e.printStackTrace();
            Assert.fail("Exception thrown");
        }
    }


    //checking raw data in log-file
    @Test
    public void testGetLogData() throws IOException {


        BufferedReader br = new BufferedReader(new FileReader(DataParser.LogReader.getProjectPath()));
        String line;
        int rows = 0;

        while ((line = br.readLine()) != null)
            rows++;

        Assert.assertEquals(rows, logData.size());

    }


    @Test
    public void testGetHexCodes(){
        try {
            Timestamp lowerBound = logReader.convertToTimeStamp("2020-08-20 17:18:03");
            Timestamp upperBound = logReader.convertToTimeStamp("2020-08-20 17:55:03");
            List<String> hexCode = logReader.getHexList(lowerBound, upperBound);

            for (int i = 0; i < hexCode.size(); i++) {
                String hex = hexCode.get(i);
               if ( !logReader.checkHexFormat(hex) && !logReader.checkHexDataLength(hex) )
                   Assert.fail("Format wrong!");
            }
            Assert.assertTrue(true);
        } catch (ParseException e){
            e.printStackTrace();
            Assert.fail("Thrown ParseException");
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Thrown IOException");
        }
    }

}